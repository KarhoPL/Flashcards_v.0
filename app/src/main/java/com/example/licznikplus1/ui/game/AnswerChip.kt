package com.example.licznikplus1.ui.game

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.licznikplus1.ui.components.AutoResizeText

@Composable
fun AnswerChip(
    text: String,
    width: Dp,
    modifier: Modifier = Modifier,
    maxFontSize: TextUnit = 28.sp
) {

    Card(
        modifier = modifier.width(width),
        shape = RoundedCornerShape(50.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        AutoResizeText(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 14.dp
                ),
            maxFontSize = maxFontSize,
            minFontSize = 16.sp,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}