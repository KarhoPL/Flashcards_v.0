package com.example.licznikplus1.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.licznikplus1.data.DatabaseProvider
import com.example.licznikplus1.data.SettingsDataStore
import kotlinx.coroutines.flow.first

class ReviewNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val settings = SettingsDataStore(applicationContext)
            .settings
            .first()

        if (!settings.reviewNotificationsEnabled) {
            return Result.success()
        }

        val database = DatabaseProvider.getDatabase(applicationContext)

        val dueCards = database
            .flashcardProgressDao()
            .getDueCardsCount(System.currentTimeMillis())
        val notificationSent = SettingsDataStore(applicationContext)
            .reviewNotificationSent
            .first()

        if (dueCards >= settings.reviewNotificationThreshold) {

            if (!notificationSent) {

                NotificationHelper.showReviewNotification(
                    applicationContext,
                    dueCards
                )

                SettingsDataStore(applicationContext)
                    .saveReviewNotificationSent(true)
            }

        } else {

            SettingsDataStore(applicationContext)
                .saveReviewNotificationSent(false)
        }
        return Result.success()
    }
}