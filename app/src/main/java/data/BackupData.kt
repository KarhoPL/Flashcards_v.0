package com.example.licznikplus1.data

import kotlinx.serialization.Serializable

@Serializable
data class BackupData(
    val app: String,

    val version: Int,

    val exportedAt: Long,

    val flashcards: List<Flashcard>,

    val flashcardProgress: List<FlashcardProgressEntity>,

    val reviewHistory: List<ReviewEventEntity>,

)