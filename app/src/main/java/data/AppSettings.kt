package com.example.licznikplus1.data

data class AppSettings(
    val questionField: FlashcardField = FlashcardField.CHINESE,
    val autoPlayAudio: Boolean = true,
    val newCardsPerDay: Int = 5,

    // Number of cards shown in free learning mode.
    val learningCardsCount: Int = 10
)