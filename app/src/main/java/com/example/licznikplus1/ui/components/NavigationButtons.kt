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
    onAgainClick: () -> Unit,
    onHardClick: () -> Unit,
    onGoodClick: () -> Unit,
    onEasyClick: () -> Unit,
) {
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