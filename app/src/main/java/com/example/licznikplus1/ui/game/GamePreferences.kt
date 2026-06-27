package com.example.licznikplus1.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.gameDataStore by preferencesDataStore(
    name = "game_preferences"
)

class GamePreferences(
    private val context: Context
) {

    companion object {
        val HIGH_SCORE = intPreferencesKey("high_score")
    }

    val highScore: Flow<Int> =
        context.gameDataStore.data.map { preferences ->
            preferences[HIGH_SCORE] ?: 0
        }

    suspend fun saveHighScore(score: Int) {

        context.gameDataStore.edit { preferences ->

            preferences[HIGH_SCORE] = score
        }
    }
}