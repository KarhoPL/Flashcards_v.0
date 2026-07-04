package com.example.licznikplus1.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "flashcards")
data class FlashcardEntity(

    @PrimaryKey
    val id: String,

    val polish: String,

    val pinyin: String,

    val chinese: String
)