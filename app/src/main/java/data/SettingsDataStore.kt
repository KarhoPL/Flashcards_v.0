package com.example.licznikplus1.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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

        private val ASK_CHINESE =
            booleanPreferencesKey("ask_chinese")

        private val ASK_POLISH =
            booleanPreferencesKey("ask_polish")

        private val ASK_AUDIO =
            booleanPreferencesKey("ask_audio")

        private val AUTO_PLAY_AUDIO =
            booleanPreferencesKey("auto_play_audio")

        private val NEW_CARDS_PER_DAY =
            intPreferencesKey("new_cards_per_day")

        private val LEARNING_CARDS_COUNT =
            intPreferencesKey("learning_cards_count")

        private val ONE_HAND_MODE =
            booleanPreferencesKey("one_hand_mode")
    }

    val settings: Flow<AppSettings> =
        context.dataStore.data.map { preferences ->

            AppSettings(
                askChinese =
                preferences[ASK_CHINESE] ?: true,

                askPolish =
                preferences[ASK_POLISH] ?: true,

                askAudio =
                preferences[ASK_AUDIO] ?: true,

                autoPlayAudio =
                preferences[AUTO_PLAY_AUDIO] ?: true,

                newCardsPerDay =
                preferences[NEW_CARDS_PER_DAY] ?: 5,

                learningCardsCount =
                preferences[LEARNING_CARDS_COUNT] ?: 10,

                oneHandMode =
                preferences[ONE_HAND_MODE] ?: false,
            )

        }

    suspend fun saveAskChinese(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ASK_CHINESE] = enabled
        }
    }

    suspend fun saveAskPolish(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ASK_POLISH] = enabled
        }
    }

    suspend fun saveAskAudio(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ASK_AUDIO] = enabled
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

    suspend fun saveOneHandMode(
        enabled: Boolean
    ) {
        context.dataStore.edit { preferences ->
            preferences[ONE_HAND_MODE] = enabled
        }
    }
}