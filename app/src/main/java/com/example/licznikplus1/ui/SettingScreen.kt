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
import com.example.licznikplus1.data.FlashcardField
import com.example.licznikplus1.ui.components.LanguageSelector

@Composable
fun SettingsScreen(
    navController: NavController,
    questionField: FlashcardField,
    onQuestionFieldChange: (FlashcardField) -> Unit,
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
        Text(
            text = "Język pytania",
            fontSize = 24.sp
        )
        LanguageSelector(
            selectedField = questionField,
            onFieldSelected = onQuestionFieldChange
        )
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