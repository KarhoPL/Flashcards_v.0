package com.example.licznikplus1.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationHelper {

    const val CHANNEL_ID = "review_notifications"

    fun createChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Powtórki",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Powiadomienia o kartach do powtórki"
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
    fun showTestNotification(
        context: Context
    ) {

        val notification =
            androidx.core.app.NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Test")
                .setContentText("Powiadomienia działają 😊")
                .setAutoCancel(true)
                .build()

        androidx.core.app.NotificationManagerCompat
            .from(context)
            .notify(1, notification)
    }
    fun showReviewNotification(
        context: Context,
        dueCards: Int
    ) {

        val notification = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Czas na powtórkę")
            .setContentText("Masz $dueCards kart gotowych do powtórki.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat
            .from(context)
            .notify(1001, notification)
    }

}