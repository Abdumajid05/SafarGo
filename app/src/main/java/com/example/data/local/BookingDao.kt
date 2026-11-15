package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun observeAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE id = :bookingId LIMIT 1")
    fun observeBookingById(bookingId: Long): Flow<BookingEntity?>

    @Query("SELECT COUNT(*) FROM bookings")
    suspend fun getBookingCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bookings: List<BookingEntity>)

    @Query("UPDATE bookings SET statusName = :statusName WHERE id = :bookingId")
    suspend fun updateBookingStatus(bookingId: Long, statusName: String)
}
