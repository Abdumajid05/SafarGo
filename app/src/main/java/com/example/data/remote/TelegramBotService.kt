package com.example.data.remote

import com.example.data.model.BookingDoc

enum class TelegramIntegrationStatus {
    SERVER_CONFIGURATION_REQUIRED
}

data class TelegramBotPublicConfig(
    val botHandle: String = "@SafarGoUzBot",
    val publicStartUrl: String = "https://t.me/SafarGoUzBot?start=safargo_auth",
    val status: TelegramIntegrationStatus = TelegramIntegrationStatus.SERVER_CONFIGURATION_REQUIRED
)

/**
 * Security boundary for Telegram integration.
 *
 * Telegram Bot API tokens are never stored in BuildConfig, APK assets, or Android client code.
 * Bot verification (/start webhook, OTP codes, and ticket notifications) must run on a trusted
 * backend server (e.g., Firebase Cloud Functions + Secret Manager).
 */
class TelegramBotService {
    val publicConfig: TelegramBotPublicConfig = TelegramBotPublicConfig()

    fun isServerConfigured(): Boolean = false

    fun statusDescriptionUz(): String {
        return "Telegram bot server funksiyasi sozlashni talab qiladi (Cloud Functions + Secret Manager)."
    }

    fun formatBookingTicketPreview(
        carrier: String,
        carrierCode: String,
        originCode: String,
        destinationCode: String,
        travelDate: String,
        departureTime: String,
        seatNumber: String,
        passengerName: String,
        totalUzs: Long
    ): String {
        return buildString {
            appendLine("SafarGo — Chipta ma’lumoti")
            appendLine("Tashuvchi: $carrier ($carrierCode)")
            appendLine("Yo‘nalish: $originCode -> $destinationCode")
            appendLine("Sana va vaqt: $travelDate · $departureTime")
            appendLine("O‘rindiq: $seatNumber")
            appendLine("Yo‘lovchi: $passengerName")
            appendLine("Summa: $totalUzs UZS")
        }.trim()
    }

    fun formatCheckInPreview(booking: BookingDoc): String {
        return buildString {
            appendLine("SafarGo — Onlayn Check-in")
            appendLine("PNR: ${booking.pnrCode}")
            appendLine("Yo‘nalish: ${booking.originCode} -> ${booking.destinationCode}")
            appendLine("Terminal / Joy: ${booking.platformOrGate} · ${booking.seatNumber}")
        }.trim()
    }
}
