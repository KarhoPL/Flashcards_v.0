package com.example.licznikplus1.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FlashcardDao {

    @Query("SELECT * FROM flashcards")
    suspend fun getAll(): List<FlashcardEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flashcard: FlashcardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        flashcards: List<FlashcardEntity>
    )

    @Query("SELECT COUNT(*) FROM flashcards")
    suspend fun getCount(): Int

    @Query("DELETE FROM flashcards")
    suspend fun deleteAll()
}