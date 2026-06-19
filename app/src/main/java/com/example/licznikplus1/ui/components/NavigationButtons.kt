package com.example.licznikplus1.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NavigationButtons(
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    randomMode: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        if (!randomMode) {
            Button(
                onClick = onPreviousClick
            ) {
                Text("Poprzednie")
            }
        }

        Button(
            onClick = onNextClick
        ) {
            Text(
                if (randomMode) {
                    "Losuj"
                } else {
                    "Następne"
                }
            )
        }
    }
}