package com.example.licznikplus1.ui.game

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.licznikplus1.ui.theme.OnPrimary

@Composable
fun GameOverDialog(
    score: Int,
    highScore: Int,
    onRestart: () -> Unit,
    onBackToMenu: () -> Unit
) {

    AlertDialog(
        onDismissRequest = {},

        title = {
            Text("Koniec gry")
        },

        text = {
            Text(
                "⭐ Wynik: $score\n\n🏆 Rekord: $highScore"
            )
        },

        confirmButton = {
            Button(
                onClick = onRestart
            ) {
                Text("Zagraj ponownie")
            }
        },

        dismissButton = {
            OutlinedButton(
                onClick = onBackToMenu
            ) {
                Text("Menu", color = OnPrimary,)
            }
        }
    )
}