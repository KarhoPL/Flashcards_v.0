package com.example.licznikplus1.data

import android.content.Context
import kotlinx.serialization.json.Json

class FlashcardRepository(
    private val context: Context
) {

    private val dao =
        DatabaseProvider
            .getDatabase(context)
            .flashcardDao()

    suspend fun getAll(): List<Flashcard> {

        return dao
            .getAll()
            .map {
                it.toFlashcard()
            }
    }

    suspend fun insertAll(
        flashcards: List<Flashcard>
    ) {

        dao.insertAll(
            flashcards.map {
                it.toEntity()
            }
        )
    }

    suspend fun deleteAll() {

        dao.deleteAll()
    }

    companion object {

        fun loadFromAssets(
            context: Context
        ): List<Flashcard> {

            val jsonString = context.assets
                .open("flashcards.json")
                .bufferedReader()
                .use {
                    it.readText()
                }

            return Json.decodeFromString(jsonString)
        }
    }
}