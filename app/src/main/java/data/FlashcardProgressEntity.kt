package com.example.licznikplus1.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard_progress")
data class FlashcardProgressEntity(

    @PrimaryKey
    val cardId: String,

    val introducedAt: Long? = null,

    val nextReviewDate: Long = 0L,
    val intervalHours: Int = 0,

    val lastReviewedAt: Long? = null
)