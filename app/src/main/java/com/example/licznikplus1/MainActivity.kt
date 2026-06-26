package com.example.licznikplus1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.licznikplus1.data.SessionMode
import com.example.licznikplus1.data.SettingsDataStore
import com.example.licznikplus1.ui.FlashcardScreen
import com.example.licznikplus1.ui.HomeScreen
import com.example.licznikplus1.ui.SettingsScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current

            val settingsDataStore = remember {
                SettingsDataStore(context)
            }

            val settings by settingsDataStore.settings.collectAsState(
                initial = com.example.licznikplus1.data.AppSettings()
            )
            val coroutineScope = rememberCoroutineScope()
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    HomeScreen(navController)
                }
                composable("learning") {
                    FlashcardScreen(
                        navController = navController,
                        questionField = settings.questionField,
                        autoPlayAudio = settings.autoPlayAudio,
                        sessionMode = SessionMode.LEARNING,
                        newCardsPerDay = settings.newCardsPerDay,
                        learningCardsCount = settings.learningCardsCount
                    )
                }

                composable("testing") {
                    FlashcardScreen(
                        navController = navController,
                        questionField = settings.questionField,
                        autoPlayAudio = settings.autoPlayAudio,
                        sessionMode = SessionMode.TESTING,
                        newCardsPerDay = settings.newCardsPerDay,
                        learningCardsCount = settings.learningCardsCount
                    )
                }

                composable("settings") {
                    SettingsScreen(
                        navController = navController,
                        questionField = settings.questionField,
                        onQuestionFieldChange = {
                            coroutineScope.launch {
                                settingsDataStore.saveQuestionField(it)
                            }
                        },
                        autoPlayAudio = settings.autoPlayAudio,
                        onAutoPlayAudioChange = {
                            coroutineScope.launch {
                                settingsDataStore.saveAutoPlayAudio(it)
                            }
                        },
                        newCardsPerDay = settings.newCardsPerDay,
                        onNewCardsPerDayChange = {
                            coroutineScope.launch {
                                settingsDataStore.saveNewCardsPerDay(it)
                            }
                        },
                        learningCardsCount = settings.learningCardsCount,
                        onLearningCardsCountChange = {
                            coroutineScope.launch {
                                settingsDataStore.saveLearningCardsCount(it)
                            }
                        }
                    )
                }
            }
        }
    }
}