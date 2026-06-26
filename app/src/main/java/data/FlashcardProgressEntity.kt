package com.example.licznikplus1.data

import androidx.room.Entity

@Entity(
    tableName = "flashcard_progress",
    primaryKeys = ["cardId", "questionField"]
)
data class FlashcardProgressEntity(

    val cardId: String,

    val questionField: FlashcardField,

    val introducedAt: Long? = null,

    val nextReviewDate: Long = 0L,

    val intervalHours: Int = 0,

    val lastReviewedAt: Long? = null
)