package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.AdminSystemConfigDoc
import com.example.data.model.BookingDoc
import com.example.data.model.BookingStatusTag
import com.example.data.model.TravelMode
import com.example.data.model.TravelOption
import com.example.data.model.UserProfileDoc
import com.example.data.remote.OperationType
import com.example.data.remote.handleFirestoreError
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirestoreTravelRepository(
    private val db: FirebaseFirestore
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    fun observeUserProfile(userId: String): Flow<UserProfileDoc?> {
        return db.collection("users").document(userId)
            .snapshots()
            .map { snapshot ->
                if (snapshot.exists()) {
                    snapshot.toObject(UserProfileDoc::class.java, ServerTimestampBehavior.ESTIMATE)
                } else {
                    null
                }
            }
    }

    fun observeBookings(userId: String): Flow<List<BookingDoc>> {
        return db.collection("users").document(userId)
            .collection("bookings")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(BookingDoc::class.java, ServerTimestampBehavior.ESTIMATE)
            }
    }

    fun observeBookingById(userId: String, bookingId: String): Flow<BookingDoc?> {
        return db.collection("users").document(userId)
            .collection("bookings").document(bookingId)
            .snapshots()
            .map { snapshot ->
                if (snapshot.exists()) {
                    snapshot.toObject(BookingDoc::class.java, ServerTimestampBehavior.ESTIMATE)
                } else {
                    null
                }
            }
    }

    suspend fun checkAdminCustomClaim(user: FirebaseUser?, forceRefresh: Boolean = false): Boolean {
        if (user == null) return false
        return try {
            val tokenResult = user.getIdToken(forceRefresh).await()
            tokenResult.claims["admin"] == true
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            false
        }
    }

    fun observeAdminConfig(): Flow<AdminSystemConfigDoc?> {
        return db.collection("adminConfig").document(PRIMARY_ADMIN_CONFIG_ID)
            .snapshots()
            .map { snapshot ->
                if (snapshot.exists()) {
                    snapshot.toObject(AdminSystemConfigDoc::class.java, ServerTimestampBehavior.ESTIMATE)
                } else {
                    null
                }
            }
    }

    suspend fun updateAdminConfig(
        telegramBotHandle: String,
        telegramWebhookConfigured: Boolean,
        maintenanceNotice: String
    ): Result<Unit> {
        val docRef = db.collection("adminConfig").document(PRIMARY_ADMIN_CONFIG_ID)
        val normalizedBot = normalizeTelegramHandle(telegramBotHandle).ifBlank { "@SafarGoUzBot" }
        val payload = mapOf(
            "telegramBotHandle" to normalizedBot.take(40),
            "telegramWebhookConfigured" to telegramWebhookConfigured,
            "maintenanceNotice" to maintenanceNotice.trim().take(240),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        return try {
            docRef.set(payload).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            Result.failure(e)
        }
    }

    private fun normalizeTelegramHandle(raw: String): String {
        val cleaned = raw.trim().replace(" ", "").removePrefix("https://t.me/").removePrefix("t.me/")
        return when {
            cleaned.isBlank() -> ""
            cleaned.startsWith("@") -> cleaned.take(40)
            else -> "@$cleaned".take(40)
        }
    }

    suspend fun registerOrUpdateProfile(
        userId: String,
        fullName: String,
        phone: String,
        passportSeries: String,
        preferredPaymentId: String,
        telegramUsername: String = "",
        telegramNotificationsEnabled: Boolean = false
    ): Result<Unit> {
        val docRef = db.collection("users").document(userId)
        val normalizedTelegram = normalizeTelegramHandle(telegramUsername)
        val cleanName = fullName.trim().ifBlank { "SafarGo Sayohatchi" }.take(80)
        return try {
            val existing = docRef.get().await()
            if (existing.exists()) {
                val updates = mapOf<String, Any>(
                    "fullName" to cleanName,
                    "phone" to phone.trim().take(24),
                    "passportSeries" to passportSeries.trim().uppercase().take(20),
                    "preferredPaymentId" to preferredPaymentId.take(30),
                    "telegramUsername" to normalizedTelegram,
                    "telegramNotificationsEnabled" to telegramNotificationsEnabled,
                    "onboardingCompleted" to true,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.update(updates).await()
            } else {
                val payload = mapOf<String, Any>(
                    "userId" to userId,
                    "fullName" to cleanName,
                    "phone" to phone.trim().take(24),
                    "passportSeries" to passportSeries.trim().uppercase().take(20),
                    "clubPoints" to 25_000L,
                    "tierLabel" to "VIP GOLD",
                    "favoriteDestinations" to listOf("SKD", "BHK"),
                    "preferredPaymentId" to preferredPaymentId.take(30),
                    "telegramUsername" to normalizedTelegram,
                    "telegramNotificationsEnabled" to telegramNotificationsEnabled,
                    "onboardingCompleted" to true,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.set(payload).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun toggleFavoriteDestination(userId: String, destinationCode: String): Result<Unit> {
        val docRef = db.collection("users").document(userId)
        return try {
            val snap = docRef.get().await()
            val profile = snap.toObject(UserProfileDoc::class.java)
            val currentFavs = profile?.favoriteDestinations ?: emptyList()
            val updated = if (destinationCode in currentFavs) {
                currentFavs - destinationCode
            } else {
                (currentFavs + destinationCode).take(20)
            }
            docRef.update(
                mapOf(
                    "favoriteDestinations" to updated,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun redeemClubPoints(userId: String, pointsToSpend: Long): Result<Unit> {
        val docRef = db.collection("users").document(userId)
        return try {
            val snap = docRef.get().await()
            val current = snap.getLong("clubPoints") ?: 0L
            val nextPoints = (current - pointsToSpend).coerceAtLeast(0L)
            docRef.update(
                mapOf(
                    "clubPoints" to nextPoints,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun updatePreferredPayment(userId: String, paymentId: String): Result<Unit> {
        val docRef = db.collection("users").document(userId)
        return try {
            docRef.update(
                mapOf(
                    "preferredPaymentId" to paymentId,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun updateTelegramBotSettings(
        userId: String,
        telegramUsername: String,
        enabled: Boolean
    ): Result<Unit> {
        val docRef = db.collection("users").document(userId)
        val normalizedTelegram = normalizeTelegramHandle(telegramUsername)
        return try {
            docRef.update(
                mapOf(
                    "telegramUsername" to normalizedTelegram,
                    "telegramNotificationsEnabled" to enabled,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun createBooking(
        userId: String,
        option: TravelOption,
        travelDate: String,
        seatNumber: String,
        seatExtraUzs: Long,
        passengerName: String,
        passportSeries: String,
        phone: String,
        paymentMethodLabel: String,
        redeemPointsUsed: Long = 0L
    ): Result<String> {
        val bookingId = "bk_${UUID.randomUUID().toString().replace("-", "").take(16)}"
        val docRef = db.collection("users").document(userId)
            .collection("bookings").document(bookingId)

        val randomNum = (100..999).random()
        val pnrPrefix = when (option.mode) {
            TravelMode.RAIL -> "AF"
            TravelMode.FLIGHTS -> "HY"
            TravelMode.HOTELS -> "HT"
            TravelMode.TRANSFERS -> "TR"
            TravelMode.TOURS -> "GU"
            TravelMode.ESIM -> "ES"
        }
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        val pnr = "$pnrPrefix-$randomNum${(1..2).map { chars.random() }.joinToString("")}"
        val ticketNum = "860-${(10000000..99999999).random()}"
        val discountUzs = redeemPointsUsed * 10L
        val finalUzs = (option.priceUzs + seatExtraUzs - discountUzs).coerceAtLeast(0L)

        val payload = mapOf(
            "pnrCode" to pnr,
            "ticketNumber" to ticketNum,
            "modeName" to option.mode.name,
            "carrier" to option.carrier.take(60),
            "carrierCode" to option.carrierCode.take(30),
            "originCode" to option.originCode.take(10),
            "originCity" to option.originCity.take(50),
            "destinationCode" to option.destinationCode.take(10),
            "destinationCity" to option.destinationCity.take(50),
            "departureTime" to option.departureTime.take(20),
            "arrivalTime" to option.arrivalTime.take(20),
            "travelDate" to travelDate.take(30),
            "durationText" to option.durationText.take(30),
            "fareClass" to option.fareClass.take(40),
            "seatNumber" to seatNumber.take(30),
            "platformOrGate" to option.platformOrGate.take(40),
            "passengerName" to passengerName.trim().uppercase().take(80),
            "passportSeries" to passportSeries.trim().uppercase().take(24),
            "phoneNumber" to phone.trim().take(24),
            "paymentMethod" to paymentMethodLabel.take(60),
            "totalUzs" to finalUzs,
            "statusName" to BookingStatusTag.CONFIRMED.name,
            "baggageInfo" to option.baggageInfo.take(80),
            "isCompleted" to false,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )

        return try {
            docRef.set(payload).await()
            // Award +500 SafarGo Club cashback points (net of redeemed points)
            val userRef = db.collection("users").document(userId)
            val snap = userRef.get().await()
            if (snap.exists()) {
                val curPts = snap.getLong("clubPoints") ?: 0L
                val nextPts = (curPts - redeemPointsUsed + 500L).coerceAtLeast(0L)
                userRef.update(
                    mapOf(
                        "clubPoints" to nextPts,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            }
            Result.success(bookingId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun checkInBooking(userId: String, bookingId: String): Result<Unit> {
        val docRef = db.collection("users").document(userId)
            .collection("bookings").document(bookingId)
        return try {
            docRef.update(
                mapOf(
                    "statusName" to BookingStatusTag.CHECKED_IN.name,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun cancelBooking(userId: String, bookingId: String): Result<Unit> {
        val docRef = db.collection("users").document(userId)
            .collection("bookings").document(bookingId)
        return try {
            docRef.update(
                mapOf(
                    "statusName" to BookingStatusTag.CANCELLED.name,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            Result.failure(e)
        }
    }

    private companion object {
        const val PRIMARY_ADMIN_CONFIG_ID = "telegram_bot"
    }
}
