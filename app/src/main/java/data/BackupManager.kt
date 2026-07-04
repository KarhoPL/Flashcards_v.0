package com.example.licznikplus1.data

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BackupManager(
    private val context: Context
) {

    private val database =
        DatabaseProvider.getDatabase(context)

    suspend fun createBackup(): BackupData {

        val flashcardRepository = FlashcardRepository(context)

        val flashcards = flashcardRepository.getAll()

        val flashcardProgress =
            database
                .flashcardProgressDao()
                .getAll()

        val reviewHistory =
            database
                .reviewEventDao()
                .getAll()

        return BackupData(
            app = "LicznikPlus1",
            version = 1,
            exportedAt = System.currentTimeMillis(),
            flashcards = flashcards,
            flashcardProgress = flashcardProgress,
            reviewHistory = reviewHistory,
        )
    }

    fun backupToJson(
        backup: BackupData
    ): String {

        return Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }.encodeToString(backup)
    }

    fun jsonToBackup(
        json: String
    ): BackupData {

        return Json {
            ignoreUnknownKeys = true
        }.decodeFromString(json)
    }

    suspend fun restoreBackup(
        backup: BackupData
    ) {
        if (backup.app != "LicznikPlus1") {
            throw IllegalArgumentException("Nieprawidłowy plik backupu.")
        }
        database.flashcardDao().deleteAll()
        database.reviewEventDao().deleteAll()
        database.flashcardProgressDao().deleteAll()

        backup.flashcards.forEach {
            database.flashcardDao().insert(it.toEntity())
        }
        backup.flashcardProgress.forEach {
            database.flashcardProgressDao().insert(it)
        }

        backup.reviewHistory.forEach {
            database.reviewEventDao().insert(it)
        }

    }
}