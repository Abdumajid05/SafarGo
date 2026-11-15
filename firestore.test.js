const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";
const ADMIN_UID = "admin_789";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

function validProfile(uid) {
  return {
    userId: uid,
    fullName: "Test Traveler",
    phone: "+998 90 000 00 01",
    passportSeries: "TT 0000001",
    clubPoints: 25000,
    tierLabel: "VIP GOLD",
    favoriteDestinations: ["SKD", "BHK"],
    preferredPaymentId: "uzcard",
    telegramUsername: "@test_traveler",
    telegramNotificationsEnabled: true,
    onboardingCompleted: true,
    createdAt: new Date(),
    updatedAt: new Date(),
  };
}

function validBooking() {
  return {
    pnrCode: "HY-023SG",
    ticketNumber: "250-778120391",
    modeName: "FLIGHTS",
    carrier: "Uzbekistan Airways",
    carrierCode: "Reysi #HY-023",
    originCode: "TAS",
    originCity: "Toshkent T3",
    destinationCode: "SKD",
    destinationCity: "Samarqand Xalqaro",
    departureTime: "14:15",
    arrivalTime: "15:10",
    travelDate: "Ertaga",
    durationText: "55 daqiqa",
    fareClass: "Ekonom",
    seatNumber: "14B",
    platformOrGate: "Terminal 3",
    passengerName: "TEST TRAVELER",
    passportSeries: "TT 0000001",
    phoneNumber: "+998 90 000 00 01",
    paymentMethod: "Uzcard (Demo)",
    totalUzs: 420000,
    statusName: "CONFIRMED",
    baggageInfo: "20 kg",
    isCompleted: false,
    createdAt: new Date(),
    updatedAt: new Date(),
  };
}

function validAdminConfig() {
  return {
    telegramBotHandle: "@SafarGoUzBot",
    telegramWebhookConfigured: false,
    maintenanceNotice: "",
    updatedAt: new Date(),
  };
}

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) await testEnv.cleanup();
});

beforeEach(async () => {
  if (testEnv) await testEnv.clearFirestore();
});

test("Unauthenticated user cannot read or write profiles, bookings, or adminConfig", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("bookings").get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).set(validProfile(ALICE_UID)));
  await assertFails(unauthDb.collection("adminConfig").doc("telegram_bot").get());
  await assertFails(unauthDb.collection("adminConfig").doc("telegram_bot").set(validAdminConfig()));
});

test("Authenticated user can create and update own profile, but cannot write telegramChatId", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const ref = aliceDb.collection("users").doc(ALICE_UID);

  // Creating with client-supplied telegramChatId must be rejected
  await assertFails(ref.set({ ...validProfile(ALICE_UID), telegramChatId: 123456789 }));

  // Clean create succeeds
  await assertSucceeds(ref.set(validProfile(ALICE_UID)));

  // Standard fields update succeeds
  await assertSucceeds(
    ref.update({
      clubPoints: 20000,
      phone: "+998 93 000 00 02",
      telegramUsername: "@alice_traveler",
      telegramNotificationsEnabled: false,
      updatedAt: new Date(),
    })
  );

  // Updating telegramChatId from client must fail
  await assertFails(
    ref.update({
      telegramChatId: 987654321,
      updatedAt: new Date(),
    })
  );
});

test("Shadow update with ghost field on profile is rejected", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bad = { ...validProfile(ALICE_UID), isAdmin: true };
  await assertFails(aliceDb.collection("users").doc(ALICE_UID).set(bad));
});

test("Cross-user access to profile or bookings is denied for non-admins", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).set(validProfile(ALICE_UID)));
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("bookings").doc("b1").set(validBooking())
  );

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.collection("users").doc(ALICE_UID).get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("bookings").get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("bookings").doc("b1").get());
});

test("Authenticated user can create, check-in, and cancel own booking, but cannot mutate cancelled booking", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bookingRef = aliceDb.collection("users").doc(ALICE_UID).collection("bookings").doc("b1");
  await assertSucceeds(bookingRef.set(validBooking()));
  await assertSucceeds(bookingRef.update({ statusName: "CHECKED_IN", updatedAt: new Date() }));
  await assertSucceeds(bookingRef.update({ statusName: "CANCELLED", updatedAt: new Date() }));
  await assertFails(bookingRef.update({ statusName: "CONFIRMED", updatedAt: new Date() }));
});

test("Non-admin cannot read or write adminConfig; custom-claim admin can manage adminConfig and audit users", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).set(validProfile(ALICE_UID)));
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("bookings").doc("b1").set(validBooking())
  );

  // Regular user cannot read or write /adminConfig
  await assertFails(aliceDb.collection("adminConfig").doc("telegram_bot").get());
  await assertFails(aliceDb.collection("adminConfig").doc("telegram_bot").set(validAdminConfig()));

  // Server-verified custom claim { admin: true } succeeds
  const adminDb = testEnv.authenticatedContext(ADMIN_UID, { admin: true }).firestore();
  const configRef = adminDb.collection("adminConfig").doc("telegram_bot");
  await assertSucceeds(configRef.set(validAdminConfig()));
  await assertSucceeds(configRef.get());
  await assertSucceeds(
    configRef.update({
      telegramWebhookConfigured: true,
      maintenanceNotice: "Scheduled sync window",
      updatedAt: new Date(),
    })
  );

  // Ghost fields on adminConfig are rejected even for admin
  await assertFails(
    configRef.set({ ...validAdminConfig(), secretBotToken: "should-never-exist" })
  );

  // Admin can read user profiles and bookings for support
  await assertSucceeds(adminDb.collection("users").doc(ALICE_UID).get());
  await assertSucceeds(adminDb.collection("users").doc(ALICE_UID).collection("bookings").doc("b1").get());
});
