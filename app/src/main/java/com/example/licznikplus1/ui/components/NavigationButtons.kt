package com.example.licznikplus1.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NavigationButtons(
    oneHandMode: Boolean,
    onAgainClick: () -> Unit,
    onHardClick: () -> Unit,
    onGoodClick: () -> Unit,
    onEasyClick: () -> Unit,
) {
    if (oneHandMode) {
        Button(onClick = onEasyClick) {
            Text("Łatwe")
        }
        Button(onClick = onGoodClick) {
            Text("Dobrze")
        }
        Button(onClick = onHardClick) {
            Text("Trudne")
        }
        Button(onClick = onAgainClick) {
            Text("Źle")
        }
    }else {
        Button(onClick = onAgainClick) {
            Text("Źle")
        }
        Button(onClick = onHardClick) {
            Text("Trudne")
        }
        Button(onClick = onGoodClick) {
            Text("Dobrze")
        }
        Button(onClick = onEasyClick) {
            Text("Łatwe")
        }
    }
}