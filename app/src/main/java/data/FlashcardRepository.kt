package com.example.licznikplus1.data

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun loadFlashcards(context: Context): List<Flashcard> {

    val jsonString = context.assets
        .open("flashcards.json")
        .bufferedReader()
        .use { it.readText() }

    return Json.decodeFromString(jsonString)
}