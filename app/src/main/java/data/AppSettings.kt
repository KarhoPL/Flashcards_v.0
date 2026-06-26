package com.example.licznikplus1.data

data class AppSettings(
    val askChinese: Boolean = true,
    val askPolish: Boolean = true,
    val askAudio: Boolean = true,
    val autoPlayAudio: Boolean = true,
    val newCardsPerDay: Int = 5,

    // Number of cards shown in free learning mode.
    val learningCardsCount: Int = 10
)