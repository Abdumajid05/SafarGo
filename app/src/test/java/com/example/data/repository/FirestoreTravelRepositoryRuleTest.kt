package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.model.BookingStatusTag
import com.example.data.model.TravelMode
import com.example.data.model.TravelOption
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FirestoreTravelRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun registerOrUpdateProfile_createsAndUpdatesProfileWithoutStarterBookings() = runBlocking {
        val uid = signInTestUser("traveler_${System.currentTimeMillis()}@example.test")
        val repository = FirestoreTravelRepository(firestore)

        val result = withTimeout(TIMEOUT_MS) {
            repository.registerOrUpdateProfile(
                userId = uid,
                fullName = "Test Traveler",
                phone = "+998 90 000 00 01",
                passportSeries = "TT 0000001",
                preferredPaymentId = "uzcard",
                telegramUsername = "@test_traveler",
                telegramNotificationsEnabled = true
            )
        }
        assertTrue(
            "registerOrUpdateProfile failed: ${result.exceptionOrNull()}",
            result.isSuccess
        )

        val profile = withTimeout(TIMEOUT_MS) {
            repository.observeUserProfile(uid).first { it != null }
        }
        assertNotNull(profile)
        assertEquals("Test Traveler", profile?.fullName)
        assertEquals(25_000L, profile?.clubPoints)
        assertEquals("@test_traveler", profile?.telegramUsername)
        assertEquals(true, profile?.telegramNotificationsEnabled)

        val bookings = withTimeout(TIMEOUT_MS) {
            repository.observeBookings(uid).first()
        }
        assertTrue("New user should start with zero bookings", bookings.isEmpty())
    }

    @Test
    fun createCheckInAndCancelBooking_succeedsForOwner() = runBlocking {
        val uid = signInTestUser("owner_${System.currentTimeMillis()}@example.test")
        val repository = FirestoreTravelRepository(firestore)

        withTimeout(TIMEOUT_MS) {
            repository.registerOrUpdateProfile(
                userId = uid,
                fullName = "Booking Owner",
                phone = "+998 90 000 00 02",
                passportSeries = "AA 0000002",
                preferredPaymentId = "humo"
            )
        }

        val sampleOption = TravelOption(
            id = "test_opt",
            mode = TravelMode.RAIL,
            carrier = "Afrosiyob",
            carrierCode = "Poyezd #762",
            originCode = "TAS",
            originCity = "Toshkent",
            destinationCode = "SKD",
            destinationCity = "Samarqand",
            departureTime = "08:00",
            arrivalTime = "10:08",
            durationText = "2s 08d",
            stopsText = "To'g'ridan-to'g'ri",
            isDirect = true,
            isMorning = true,
            fareClass = "Ekonom",
            statusTag = BookingStatusTag.CONFIRMED,
            priceUzs = 145_000L,
            seatsLeft = 10,
            baggageInfo = "20 kg",
            amenities = emptyList(),
            platformOrGate = "Platforma 2"
        )

        val created = withTimeout(TIMEOUT_MS) {
            repository.createBooking(
                userId = uid,
                option = sampleOption,
                travelDate = "18 Noy, 2026",
                seatNumber = "04A",
                seatExtraUzs = 0L,
                passengerName = "BOOKING OWNER",
                passportSeries = "AA 0000002",
                phone = "+998 90 000 00 02",
                paymentMethodLabel = "Humo (Demo)"
            )
        }
        assertTrue(created.isSuccess)
        val bookingId = created.getOrThrow()

        val checkIn = withTimeout(TIMEOUT_MS) {
            repository.checkInBooking(uid, bookingId)
        }
        assertTrue(checkIn.isSuccess)

        val cancel = withTimeout(TIMEOUT_MS) {
            repository.cancelBooking(uid, bookingId)
        }
        assertTrue(cancel.isSuccess)
    }

    @Test
    fun nonAdminCannotAccessOrModifyAdminConfig_andClientCannotWriteTelegramChatId() = runBlocking {
        val uid = signInTestUser("standard_${System.currentTimeMillis()}@example.test")
        val repository = FirestoreTravelRepository(firestore)

        withTimeout(TIMEOUT_MS) {
            repository.registerOrUpdateProfile(
                userId = uid,
                fullName = "Standard User",
                phone = "+998 90 000 00 03",
                passportSeries = "BB 0000003",
                preferredPaymentId = "uzcard",
                telegramUsername = "@standard_user",
                telegramNotificationsEnabled = true
            )
        }

        // Standard user has no admin custom claim
        val isAdmin = withTimeout(TIMEOUT_MS) {
            repository.checkAdminCustomClaim(auth.currentUser, forceRefresh = true)
        }
        assertFalse(isAdmin)

        // Updating adminConfig without admin custom claim must fail with PERMISSION_DENIED
        val writeResult = withTimeout(TIMEOUT_MS) {
            repository.updateAdminConfig(
                telegramBotHandle = "@SafarGoUzBot",
                telegramWebhookConfigured = true,
                maintenanceNotice = "Unauthorized"
            )
        }
        assertTrue(writeResult.isFailure)
        assertEquals(
            FirebaseFirestoreException.Code.PERMISSION_DENIED,
            writeResult.exceptionOrNull()?.unwrapFirestoreException()?.code
        )

        // Reading adminConfig without admin custom claim must fail with PERMISSION_DENIED
        var readCaught: Throwable? = null
        try {
            withTimeout(FLOW_TIMEOUT_MS) {
                repository.observeAdminConfig().first()
            }
        } catch (e: Throwable) {
            readCaught = e
        }
        assertEquals(
            FirebaseFirestoreException.Code.PERMISSION_DENIED,
            readCaught?.unwrapFirestoreException()?.code
        )

        // Client attempt to mutate telegramChatId directly on users/{uid} must fail
        var chatIdCaught: Throwable? = null
        try {
            withTimeout(TIMEOUT_MS) {
                firestore.collection("users").document(uid)
                    .update("telegramChatId", 987654321L)
                    .await()
            }
        } catch (e: Throwable) {
            chatIdCaught = e
        }
        assertEquals(
            FirebaseFirestoreException.Code.PERMISSION_DENIED,
            chatIdCaught?.unwrapFirestoreException()?.code
        )
    }

    @Test
    fun crossUserRead_deniedWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser("alice_${System.currentTimeMillis()}@example.test")
        val aliceRepo = FirestoreTravelRepository(firestore)
        withTimeout(TIMEOUT_MS) {
            aliceRepo.registerOrUpdateProfile(aliceUid, "User A", "+998 90 000 00 04", "CC 0000004", "uzcard")
        }

        signInTestUser("bob_${System.currentTimeMillis()}@example.test")
        val bobRepo = FirestoreTravelRepository(firestore)

        var caught: Throwable? = null
        try {
            withTimeout(FLOW_TIMEOUT_MS) {
                bobRepo.observeBookings(aliceUid).first()
            }
        } catch (e: Throwable) {
            caught = e
        }
        val firestoreEx = caught?.unwrapFirestoreException()
        assertNotNull("Expected FirebaseFirestoreException, got $caught", firestoreEx)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreEx?.code)
    }

    @Test
    fun unauthenticatedRead_deniedWithPermissionDenied() = runBlocking {
        auth.signOut()
        val repository = FirestoreTravelRepository(firestore)

        var caught: Throwable? = null
        try {
            withTimeout(FLOW_TIMEOUT_MS) {
                repository.observeBookings("some_uid_123").first()
            }
        } catch (e: Throwable) {
            caught = e
        }
        val firestoreEx = caught?.unwrapFirestoreException()
        assertNotNull("Expected FirebaseFirestoreException, got $caught", firestoreEx)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreEx?.code)
    }

    private companion object {
        const val TIMEOUT_MS = 5000L
        const val FLOW_TIMEOUT_MS = 3000L
    }
}
