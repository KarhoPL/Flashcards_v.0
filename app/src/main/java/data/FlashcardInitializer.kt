package com.example.licznikplus1.data

import android.content.Context

class FlashcardInitializer(
    private val context: Context
) {

    suspend fun initialize() {

        val database = DatabaseProvider.getDatabase(context)

        val dao = database.flashcardDao()

        if (dao.getCount() > 0) {
            return
        }

        val flashcards =
            FlashcardRepository
                .loadFromAssets(context)

        dao.insertAll(
            flashcards.map { it.toEntity() }
        )
        println("Do bazy dodano ${dao.getCount()} fiszek.")
    }

}