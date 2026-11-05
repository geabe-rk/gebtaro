package com.example.geb_taro

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BookingBootReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {

            BookingReminderManager.rescheduleAll(
                context
            )
        }
    }
}