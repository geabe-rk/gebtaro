package com.example.geb_taro

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object BookingNotificationHelper {

    private const val CHANNEL_ID =
        "izireps_booking_reminders"

    private const val CHANNEL_NAME =
        "Pengingat Booking"

    fun createChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description =
                    "Pengingat 30 menit sebelum jadwal bermain PlayStation"
            }

            val manager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }

    fun hasNotificationPermission(
        context: Context
    ): Boolean {

        return Build.VERSION.SDK_INT <
                Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun showBookingReminder(
        context: Context,
        booking: BookingReminderItem
    ) {
        createChannel(context)

        // Jangan tampilkan notifikasi jika izin belum diberikan.
        if (!hasNotificationPermission(context)) {
            return
        }

        val openAppIntent = Intent(
            context,
            BaseActivity::class.java
        ).apply {
            flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val contentPendingIntent =
            PendingIntent.getActivity(
                context,
                booking.id.hashCode(),
                openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        val notification =
            NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )
                .setSmallIcon(R.drawable.ic_nav_booking)
                .setContentTitle(
                    "IZIReps — Pengingat Booking"
                )
                .setContentText(
                    "Jadwal ${booking.console} dimulai 30 menit lagi."
                )
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            "Jangan sampai terlewat! " +
                                    "Jadwal bermain ${booking.console} " +
                                    "kamu dimulai 30 menit lagi, " +
                                    "pukul ${
                                        BookingReminderManager
                                            .formatDateTime(
                                                booking.dateTimeMillis
                                            )
                                    }. Siapkan dirimu dan nikmati permainan!"
                        )
                )
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .setAutoCancel(true)
                .setContentIntent(contentPendingIntent)
                .build()

        NotificationManagerCompat
            .from(context)
            .notify(
                booking.id.hashCode(),
                notification
            )
    }
}