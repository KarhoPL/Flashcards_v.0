package com.example.licznikplus1.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "settings"
)

class SettingsDataStore(
    private val context: Context
) {

    companion object {

        private val QUESTION_FIELD =
            stringPreferencesKey("question_field")

        private val AUTO_PLAY_AUDIO =
            booleanPreferencesKey("auto_play_audio")

        private val NEW_CARDS_PER_DAY =
            intPreferencesKey("new_cards_per_day")

        private val LEARNING_CARDS_COUNT =
            intPreferencesKey("learning_cards_count")
    }

    val settings: Flow<AppSettings> =
        context.dataStore.data.map { preferences ->

            AppSettings(
                questionField =
                FlashcardField.valueOf(
                    preferences[QUESTION_FIELD]
                        ?: FlashcardField.CHINESE.name
                ),

                autoPlayAudio =
                preferences[AUTO_PLAY_AUDIO] ?: true,

                newCardsPerDay =
                preferences[NEW_CARDS_PER_DAY] ?: 5,

                learningCardsCount =
                preferences[LEARNING_CARDS_COUNT] ?: 10
            )

        }

    suspend fun saveQuestionField(
        field: FlashcardField
    ) {
        context.dataStore.edit { preferences ->
            preferences[QUESTION_FIELD] = field.name
        }
    }

    suspend fun saveAutoPlayAudio(
        enabled: Boolean
    ) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_PLAY_AUDIO] = enabled
        }
    }

    suspend fun saveNewCardsPerDay(
        value: Int
    ) {
        context.dataStore.edit { preferences ->
            preferences[NEW_CARDS_PER_DAY] = value
        }
    }

    suspend fun saveLearningCardsCount(
        value: Int
    ) {
        context.dataStore.edit { preferences ->
            preferences[LEARNING_CARDS_COUNT] = value
        }
    }
}