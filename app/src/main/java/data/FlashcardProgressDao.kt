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

    @Query("""
    SELECT * 
    FROM flashcard_progress
    WHERE nextReviewDate <= :now
""")
    suspend fun getDueCards(now: Long): List<FlashcardProgressEntity>

    @Query("""
    SELECT COUNT(*) 
    FROM flashcard_progress
    WHERE introducedAt >= :startOfDay
""")
    suspend fun countIntroducedToday(startOfDay: Long): Int

    @Query("""
    SELECT cardId
    FROM flashcard_progress
""")
    suspend fun getIntroducedCardIds(): List<String>

    @Query("""
    SELECT *
    FROM flashcard_progress
    WHERE introducedAt >= :startOfDay
""")
    suspend fun getIntroducedToday(
        startOfDay: Long
    ): List<FlashcardProgressEntity>

    @Query("""
    SELECT *
    FROM flashcard_progress
    ORDER BY nextReviewDate ASC
""")
    suspend fun getOrderedByNextReviewDate(): List<FlashcardProgressEntity>
}

