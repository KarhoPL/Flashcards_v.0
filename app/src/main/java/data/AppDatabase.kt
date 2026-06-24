package com.example.licznikplus1.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ReviewEventEntity::class,
        FlashcardProgressEntity::class
    ],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reviewEventDao(): ReviewEventDao

    abstract fun flashcardProgressDao(): FlashcardProgressDao
}