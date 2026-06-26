package com.example.licznikplus1.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    navController: NavController,
    askChinese: Boolean,
    onAskChineseChange: (Boolean) -> Unit,
    askPolish: Boolean,
    onAskPolishChange: (Boolean) -> Unit,
    askAudio: Boolean,
    onAskAudioChange: (Boolean) -> Unit,
    autoPlayAudio: Boolean,
    onAutoPlayAudioChange: (Boolean) -> Unit,
    newCardsPerDay: Int,
    onNewCardsPerDayChange: (Int) -> Unit,
    learningCardsCount: Int,
    onLearningCardsCountChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text("← Wróć")
        }
        Button(
            onClick = {
                onAskChineseChange(!askChinese)
            }
        ) {
            Text(
                if (askChinese) {
                    "Chiński: włączony"
                } else {
                    "Chiński: wyłączony"
                }
            )
        }

        Button(
            onClick = {
                onAskPolishChange(!askPolish)
            }
        ) {
            Text(
                if (askPolish) {
                    "Polski: włączony"
                } else {
                    "Polski: wyłączony"
                }
            )
        }

        Button(
            onClick = {
                onAskAudioChange(!askAudio)
            }
        ) {
            Text(
                if (askAudio) {
                    "Audio: włączone"
                } else {
                    "Audio: wyłączone"
                }
            )
        }
        Button(
            onClick = {
                onAutoPlayAudioChange(!autoPlayAudio)
            }
        ) {
            Text(
                if (autoPlayAudio) {
                    "Czytaj po odkryciu: włączone"
                } else {
                    "Czytaj po odkryciu: wyłączone"
                }
            )
        }
        Text(
            text = "Nowe karty dziennie: $newCardsPerDay",
            fontSize = 24.sp
        )

        Row {
            Button(
                onClick = {
                    if (newCardsPerDay > 0) {
                        onNewCardsPerDayChange(newCardsPerDay - 1)
                    }
                }
            ) {
                Text("-")
            }

            Button(
                onClick = {
                    onNewCardsPerDayChange(newCardsPerDay + 1)
                }
            ) {
                Text("+")
            }
        }

        Row {
            Button(
                onClick = {
                    if (learningCardsCount > 0) {
                        onLearningCardsCountChange(learningCardsCount - 1)
                    }
                }
            ) {
                Text("-")
            }

            Button(
                onClick = {
                    onLearningCardsCountChange(learningCardsCount + 1)
                }
            ) {
                Text("+")
            }
        }

    }
}