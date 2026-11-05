package com.example.geb_taro

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class BookingReminderItem(
    val id: Long,
    val console: String,
    val dateTimeMillis: Long,
    val duration: String,
    val status: String = "Menunggu"
)

object BookingReminderManager {

    private const val PREFS_NAME = "izireps_booking_reminders"
    private const val KEY_BOOKINGS = "bookings_json"

    private const val REMINDER_BEFORE_MILLIS =
        30 * 60 * 1000L

    private fun prefs(context: Context) =
        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

    fun getBookings(
        context: Context
    ): List<BookingReminderItem> {

        val raw = prefs(context)
            .getString(KEY_BOOKINGS, "[]") ?: "[]"

        return try {
            val array = JSONArray(raw)

            buildList {
                for (index in 0 until array.length()) {
                    val item = array.getJSONObject(index)

                    add(
                        BookingReminderItem(
                            id = item.getLong("id"),
                            console = item.getString("console"),
                            dateTimeMillis =
                                item.getLong("dateTimeMillis"),
                            duration = item.getString("duration"),
                            status = item.optString(
                                "status",
                                "Menunggu"
                            )
                        )
                    )
                }
            }.sortedByDescending {
                it.dateTimeMillis
            }

        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveBooking(
        context: Context,
        booking: BookingReminderItem
    ) {
        val bookings = getBookings(context)
            .filterNot { it.id == booking.id }
            .toMutableList()

        bookings.add(booking)

        val array = JSONArray()

        bookings.forEach { item ->
            array.put(
                JSONObject().apply {
                    put("id", item.id)
                    put("console", item.console)
                    put(
                        "dateTimeMillis",
                        item.dateTimeMillis
                    )
                    put("duration", item.duration)
                    put("status", item.status)
                }
            )
        }

        prefs(context)
            .edit()
            .putString(KEY_BOOKINGS, array.toString())
            .apply()
    }

    fun getBooking(
        context: Context,
        id: Long
    ): BookingReminderItem? {
        return getBookings(context)
            .firstOrNull { it.id == id }
    }

    fun scheduleReminder(
        context: Context,
        booking: BookingReminderItem
    ): Boolean {

        val triggerAt =
            booking.dateTimeMillis - REMINDER_BEFORE_MILLIS

        // Jangan jadwalkan jika waktu pengingat sudah lewat.
        if (triggerAt <= System.currentTimeMillis()) {
            return false
        }

        val alarmManager = context.getSystemService(
            Context.ALARM_SERVICE
        ) as AlarmManager

        val intent = Intent(
            context,
            BookingReminderReceiver::class.java
        ).apply {
            action =
                BookingReminderReceiver.ACTION_BOOKING_REMINDER

            putExtra(
                BookingReminderReceiver.EXTRA_BOOKING_ID,
                booking.id
            )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            booking.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        // Alarm tidak exact agar tidak memerlukan
        // izin khusus untuk exact alarm.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        }

        return true
    }

    fun cancelReminder(
        context: Context,
        bookingId: Long
    ) {
        val alarmManager = context.getSystemService(
            Context.ALARM_SERVICE
        ) as AlarmManager

        val intent = Intent(
            context,
            BookingReminderReceiver::class.java
        ).apply {
            action =
                BookingReminderReceiver.ACTION_BOOKING_REMINDER

            putExtra(
                BookingReminderReceiver.EXTRA_BOOKING_ID,
                bookingId
            )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            bookingId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    fun cancelBooking(
        context: Context,
        bookingId: Long
    ) {
        // Batalkan alarm terlebih dahulu.
        cancelReminder(context, bookingId)

        // Hapus booking dari penyimpanan lokal.
        val remaining = getBookings(context)
            .filterNot { it.id == bookingId }

        val array = JSONArray()

        remaining.forEach { item ->
            array.put(
                JSONObject().apply {
                    put("id", item.id)
                    put("console", item.console)
                    put(
                        "dateTimeMillis",
                        item.dateTimeMillis
                    )
                    put("duration", item.duration)
                    put("status", item.status)
                }
            )
        }

        prefs(context)
            .edit()
            .putString(KEY_BOOKINGS, array.toString())
            .apply()
    }

    fun rescheduleAll(context: Context) {
        getBookings(context)
            .filter {
                it.dateTimeMillis >
                        System.currentTimeMillis()
            }
            .forEach {
                scheduleReminder(context, it)
            }
    }

    fun formatDateTime(
        timeMillis: Long
    ): String {
        return SimpleDateFormat(
            "dd MMM yyyy, HH:mm",
            Locale("id", "ID")
        ).format(Date(timeMillis))
    }
}