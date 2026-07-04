package com.example.licznikplus1.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReviewEventDao {

    @Insert
    suspend fun insert(reviewEvent: ReviewEventEntity)

    @Query("SELECT * FROM review_events")
    suspend fun getAll(): List<ReviewEventEntity>

    @Query("SELECT COUNT(*) FROM review_events")
    suspend fun getCount(): Int

    @Query("DELETE FROM review_events")
    suspend fun deleteAll()
}