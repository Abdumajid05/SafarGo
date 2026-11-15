# SafarGo Setup & Configuration Guide

This guide covers building, running tests, configuring Firebase Authentication / Firestore, and wiring optional backend services (Telegram Bot webhook and Admin Custom Claims) **without storing secrets in client code**.

---

## 1. Prerequisites

- **Android SDK**: `compileSdk = 36`, `targetSdk = 36`, `minSdk = 24`, JDK 11+
- **Gradle**: Invoke `gradle` directly from the project root (do not use `./gradlew`).
- **Node.js & Firebase CLI**: Required to run local Auth & Firestore emulator security rule tests (`firebase emulators:exec`).

---

## 2. Firebase Project & Google Sign-In

1. Place your project's `google-services.json` inside `app/` (`app/google-services.json`).
2. Ensure Google Sign-In generates the `default_web_client_id` string resource automatically from `google-services.json` (used by `CredentialManager` in `AuthAndRegistrationScreen.kt`).
3. Verify the target Firestore database ID matches `firebase.json` (`ai-studio-android-safargo-dc2e1397-ecad-40a9-950d-54da09eb54cc`) or your configured `firestore_database_id` resource.
4. Enable **Google** as a Sign-in provider in Firebase Authentication.

### Optional: Email/Password & Phone Auth
Currently, `Login / Parol` and `Telefon (+998)` render interactive validation and honest "not configured" notices. To activate them in production:
- Enable **Email/Password** and/or **Phone** providers in Firebase Console -> Authentication -> Sign-in method.
- Wire `Firebase.auth.signInWithEmailAndPassword(...)` / `createUserWithEmailAndPassword(...)` / `sendPasswordResetEmail(...)` or `PhoneAuthProvider` inside `AuthAndRegistrationScreen.kt`.
- Never embed hardcoded test passwords or credentials in source code, `BuildConfig`, or docs.

---

## 3. Cloud Firestore Security Rules (`firestore.rules`)

SafarGo enforces strict server-side validation and default-deny access in `firestore.rules`:
- **Default**: `allow read, write: if false;`
- **User Profiles (`/users/{userId}`)**: Owner (`request.auth.uid == userId`) or Admin (`request.auth.token.admin == true`) can read; owner can create/update/delete only validated schema fields (`userId`, `fullName`, `phone`, `passportSeries`, `clubPoints`, `tierLabel`, `favoriteDestinations`, `preferredPaymentId`, `telegramUsername`, `telegramNotificationsEnabled`, `onboardingCompleted`, timestamps). Note that `telegramChatId` is server-only and cannot be forged from the client.
- **Bookings (`/users/{userId}/bookings/{bookingId}`)**: Owner or Admin can read; owner can create validated bookings and update only `statusName`, `isCompleted`, `seatNumber`, `updatedAt` (provided the booking is not already `CANCELLED`).
- **Admin Config (`/adminConfig/{configId}`)**: Read/write restricted strictly to `isAdmin()` (`request.auth.token.admin == true`).

Deploy rules from Firebase CLI:
```bash
firebase deploy --only firestore:rules
```

---

## 4. Admin Privileges (Server-Side Custom Claims)

Admin access in `ProfileScreen` / `AdminConsoleSection` is verified via `FirestoreTravelRepository.isCurrentUserAdmin()`, which refreshes the Firebase ID token and checks `tokenResult.claims["admin"] == true`—never by client email matching or hardcoded passwords.

To grant admin privileges to a specific user UID using the **Firebase Admin SDK** (from a trusted admin script or Cloud Function):

```js
const admin = require("firebase-admin");
admin.initializeApp();

async function grantAdminClaim(uid) {
  await admin.auth().setCustomUserClaims(uid, { admin: true });
}
```

After setting the claim, tap **Re-check admin access** (or sign out and back in) in the SafarGo Profile / Login Menu sheet to force-refresh the ID token.

---

## 5. Telegram Bot Integration (Cloud Functions + Secret Manager)

**Security Rule:** Never store a Telegram Bot Token in `BuildConfig`, `.env` bundled into the APK, `strings.xml`, or Kotlin code. Direct client calls to `https://api.telegram.org/bot...` are intentionally omitted.

`TelegramBotService.kt` exposes only the public handle (`@SafarGoUzBot`) and `SERVER_CONFIGURATION_REQUIRED` status. To enable automated `/start` account linking and ticket QR notifications:

1. Store your Bot Token securely in **Google Cloud Secret Manager** (e.g., `TELEGRAM_BOT_TOKEN`).
2. Create a **Firebase Cloud Function (HTTPS webhook + Firestore trigger)**:
   - Bind the Secret Manager secret via `secrets: ["TELEGRAM_BOT_TOKEN"]`.
   - Handle Telegram `/start` webhook updates to verify the user link and write `telegramChatId` server-side into `/users/{userId}` using the Firebase Admin SDK.
   - Listen to `/users/{userId}/bookings/{bookingId}` writes when `telegramNotificationsEnabled == true` and send formatted notifications from the Cloud Function.
3. From the SafarGo Admin Console (signed in with `admin: true`), set the public bot `@username` and toggle **Server Cloud Function webhook configured** once deployed.

---

## 6. Secrets Policy

`.env.example` exists solely for third-party plugin compatibility. Do **not** place Telegram bot tokens, admin passwords, or private service account keys in `.env`, `local.properties`, `metadata.json`, or source files.

---

## 7. Running Local Verification

Run unit tests, localization parity checks, Compose UI Robolectric tests, and Firestore Security Rule tests against local emulators:

```bash
firebase emulators:exec --only firestore,auth --project demo-no-project 'gradle :app:testDebugUnitTest --no-daemon'
```
