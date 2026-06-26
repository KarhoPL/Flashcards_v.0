package com.example.licznikplus1.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review_events")
data class ReviewEventEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val cardId: String,

    val questionField: FlashcardField,

    val timestamp: Long,

    val rating: ReviewRating
)