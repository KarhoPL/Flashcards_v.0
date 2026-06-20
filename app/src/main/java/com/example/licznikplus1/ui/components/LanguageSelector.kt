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
import com.example.licznikplus1.data.FlashcardField

@Composable
fun LanguageSelector(
    selectedField: FlashcardField,
    onFieldSelected: (FlashcardField) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        LanguageButton(
            label = "中文",
            field = FlashcardField.CHINESE,
            selectedField = selectedField,
            onFieldSelected = onFieldSelected
        )

        LanguageButton(
            label = "Pinyin",
            field = FlashcardField.PINYIN,
            selectedField = selectedField,
            onFieldSelected = onFieldSelected
        )

        LanguageButton(
            label = "PL",
            field = FlashcardField.POLISH,
            selectedField = selectedField,
            onFieldSelected = onFieldSelected
        )

        LanguageButton(
            label = "🔊",
            field = FlashcardField.AUDIO,
            selectedField = selectedField,
            onFieldSelected = onFieldSelected
        )
    }
}

@Composable
private fun LanguageButton(
    label: String,
    field: FlashcardField,
    selectedField: FlashcardField,
    onFieldSelected: (FlashcardField) -> Unit
) {
    Button(
        onClick = {
            onFieldSelected(field)
        }
    ) {
        Text(
            if (selectedField == field) {
                "✓$label"
            } else {
                label
            }
        )
    }
}