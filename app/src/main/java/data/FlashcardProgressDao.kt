package com.example.licznikplus1.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FlashcardProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(progress: FlashcardProgressEntity)

    @Query("SELECT * FROM flashcard_progress WHERE cardId = :cardId")
    suspend fun getByCardId(cardId: String): FlashcardProgressEntity?

    @Query("SELECT * FROM flashcard_progress")
    suspend fun getAll(): List<FlashcardProgressEntity>
}