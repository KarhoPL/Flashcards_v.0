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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "flashcards"
            ) {

                composable("flashcards") {
                    FlashcardScreen(navController)
                }

                composable("settings") {
                    SettingsScreen()
                }
            }
        }
    }
}