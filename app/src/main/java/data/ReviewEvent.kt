package com.example.licznikplus1.data

data class ReviewEvent(
    val cardId: String,
    val timestamp: Long,
    val rating: ReviewRating
)

enum class ReviewRating {
    AGAIN,
    HARD,
    GOOD,
    EASY
}