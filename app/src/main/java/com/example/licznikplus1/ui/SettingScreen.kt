package com.example.licznikplus1.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.licznikplus1.data.FlashcardField
import com.example.licznikplus1.ui.components.LanguageSelector

@Composable
fun SettingsScreen(
    navController: NavController,
    questionField: FlashcardField,
    onQuestionFieldChange: (FlashcardField) -> Unit,
    randomMode: Boolean,
    onRandomModeChange: (Boolean) -> Unit
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
                onRandomModeChange(!randomMode)
            }
        ) {
            Text(
                if (randomMode) {
                    "Tryb: losowo"
                } else {
                    "Tryb: po kolei"
                }
            )
        }

    }
}