package com.example.licznikplus1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.licznikplus1.ui.FlashcardScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.licznikplus1.ui.FlashcardScreen
import com.example.licznikplus1.ui.SettingsScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.licznikplus1.data.FlashcardField
import com.example.licznikplus1.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var questionField by rememberSaveable {
                mutableStateOf(FlashcardField.CHINESE)
            }
            var autoPlayAudio by rememberSaveable {
                mutableStateOf(false)
            }

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    HomeScreen(navController)
                }
                composable("flashcards") {
                    FlashcardScreen(
                        navController = navController,
                        questionField = questionField,
                        autoPlayAudio = autoPlayAudio
                    )
                }

                composable("settings") {
                    SettingsScreen(
                        navController = navController,
                        questionField = questionField,
                        onQuestionFieldChange = { questionField = it},
                        autoPlayAudio = autoPlayAudio,
                        onAutoPlayAudioChange = { autoPlayAudio = it },
                    )
                }
            }
        }
    }
}