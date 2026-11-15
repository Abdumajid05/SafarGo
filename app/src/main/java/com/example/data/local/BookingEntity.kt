package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.BookingStatusTag
import com.example.data.model.TravelMode

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pnrCode: String,
    val ticketNumber: String,
    val modeName: String,
    val carrier: String,
    val carrierCode: String,
    val originCode: String,
    val originCity: String,
    val destinationCode: String,
    val destinationCity: String,
    val departureTime: String,
    val arrivalTime: String,
    val travelDate: String,
    val durationText: String,
    val fareClass: String,
    val seatNumber: String,
    val platformOrGate: String,
    val passengerName: String,
    val passportSeries: String,
    val phoneNumber: String,
    val paymentMethod: String,
    val totalUzs: Long,
    val statusName: String,
    val baggageInfo: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    val mode: TravelMode
        get() = runCatching { TravelMode.valueOf(modeName) }.getOrDefault(TravelMode.RAIL)

    val statusTag: BookingStatusTag
        get() = runCatching { BookingStatusTag.valueOf(statusName) }.getOrDefault(BookingStatusTag.CONFIRMED)
}
