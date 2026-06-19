package com.example.licznikplus1.data

import kotlinx.serialization.Serializable

enum class FlashcardField {
    POLISH,
    PINYIN,
    CHINESE
}

@Serializable
data class Flashcard(
    val polish: String,
    val pinyin: String,
    val chinese: String
)

fun Flashcard.getField(field: FlashcardField): String {
    return when (field) {
        FlashcardField.POLISH -> polish
        FlashcardField.PINYIN -> pinyin
        FlashcardField.CHINESE -> chinese
    }
}