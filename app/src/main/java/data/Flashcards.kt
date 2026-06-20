package com.example.licznikplus1.data

import kotlinx.serialization.Serializable

enum class FlashcardField {
    POLISH,
    PINYIN,
    CHINESE,
    AUDIO
}

@Serializable
data class Flashcard(
    val id: String,
    val polish: String,
    val pinyin: String,
    val chinese: String
)

fun Flashcard.getField(field: FlashcardField): String {
    return when (field) {
        FlashcardField.POLISH -> polish
        FlashcardField.PINYIN -> pinyin
        FlashcardField.CHINESE -> chinese
        FlashcardField.AUDIO -> "🔊"
    }
}

fun FlashcardField.isText(): Boolean {
    return this != FlashcardField.AUDIO
}

fun FlashcardField.hasAudio(): Boolean {
    return this == FlashcardField.CHINESE ||
            this == FlashcardField.AUDIO
}