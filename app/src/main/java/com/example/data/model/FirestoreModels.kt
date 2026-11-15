package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class UserProfileDoc(
    @DocumentId val id: String = "",
    val userId: String = "",
    val fullName: String = "",
    val phone: String = "",
    val passportSeries: String = "",
    val clubPoints: Long = 25_000L,
    val tierLabel: String = "VIP GOLD",
    val favoriteDestinations: List<String> = listOf("SKD"),
    val preferredPaymentId: String = "uzcard",
    val telegramUsername: String = "",
    val telegramChatId: Long = 0L,
    val telegramNotificationsEnabled: Boolean = false,
    val onboardingCompleted: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    val firstName: String
        get() = fullName.trim().split(" ").firstOrNull()?.takeIf { it.isNotBlank() } ?: "Mehmon"
}

@IgnoreExtraProperties
data class AdminSystemConfigDoc(
    @DocumentId val id: String = "",
    val telegramBotHandle: String = "@SafarGoUzBot",
    val telegramWebhookConfigured: Boolean = false,
    val maintenanceNotice: String = "",
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class BookingDoc(
    @DocumentId val id: String = "",
    val pnrCode: String = "",
    val ticketNumber: String = "",
    val modeName: String = TravelMode.FLIGHTS.name,
    val carrier: String = "",
    val carrierCode: String = "",
    val originCode: String = "",
    val originCity: String = "",
    val destinationCode: String = "",
    val destinationCity: String = "",
    val departureTime: String = "",
    val arrivalTime: String = "",
    val travelDate: String = "",
    val durationText: String = "",
    val fareClass: String = "",
    val seatNumber: String = "",
    val platformOrGate: String = "",
    val passengerName: String = "",
    val passportSeries: String = "",
    val phoneNumber: String = "",
    val paymentMethod: String = "",
    val totalUzs: Long = 0L,
    val statusName: String = BookingStatusTag.CONFIRMED.name,
    val baggageInfo: String = "",
    val isCompleted: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    val mode: TravelMode
        get() = runCatching { TravelMode.valueOf(modeName) }.getOrDefault(TravelMode.FLIGHTS)

    val statusTag: BookingStatusTag
        get() = runCatching { BookingStatusTag.valueOf(statusName) }.getOrDefault(BookingStatusTag.CONFIRMED)
}
