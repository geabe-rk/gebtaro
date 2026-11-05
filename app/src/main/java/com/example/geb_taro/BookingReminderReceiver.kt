package com.example.geb_taro

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BookingReminderReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        if (intent.action != ACTION_BOOKING_REMINDER) {
            return
        }

        val bookingId = intent.getLongExtra(
            EXTRA_BOOKING_ID,
            -1L
        )

        if (bookingId < 0L) {
            return
        }

        // Cari data booking berdasarkan ID.
        val booking =
            BookingReminderManager.getBooking(
                context,
                bookingId
            ) ?: return

        // Abaikan jika waktu booking sudah lewat.
        if (
            booking.dateTimeMillis <=
            System.currentTimeMillis()
        ) {
            return
        }

        // Tampilkan notifikasi pengingat.
        BookingNotificationHelper.showBookingReminder(
            context,
            booking
        )
    }

    companion object {

        const val ACTION_BOOKING_REMINDER =
            "com.example.geb_taro.ACTION_BOOKING_REMINDER"

        const val EXTRA_BOOKING_ID =
            "extra_booking_id"
    }
}