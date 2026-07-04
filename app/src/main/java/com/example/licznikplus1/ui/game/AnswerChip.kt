package com.example.licznikplus1.ui.game

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.licznikplus1.ui.components.AutoResizeText

@Composable
fun AnswerChip(
    text: String,
    modifier: Modifier = Modifier,
    maxFontSize: TextUnit = 24.sp,
    containerColor: Color = MaterialTheme.colorScheme.surface
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {

        AutoResizeText(
            text = text,
            modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 14.dp
                ),
            maxFontSize = maxFontSize,
            minFontSize = 20.sp,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}