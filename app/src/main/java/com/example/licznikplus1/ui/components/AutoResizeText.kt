package com.example.licznikplus1.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun AutoResizeText(
    text: String,
    modifier: Modifier = Modifier,
    maxFontSize: TextUnit,
    minFontSize: TextUnit = 14.sp,
    maxLines: Int = 1,
    textAlign: TextAlign = TextAlign.Start
) {

    BoxWithConstraints(modifier) {

        val density = LocalDensity.current
        val textMeasurer = rememberTextMeasurer()

        val maxWidthPx = with(density) {
            maxWidth.roundToPx()
        }

        val fontSize = remember(text, maxWidthPx) {

            var size = maxFontSize

            while (size.value > minFontSize.value) {

                val result = textMeasurer.measure(
                    text = AnnotatedString(text),
                    style = TextStyle(fontSize = size),
                    maxLines = maxLines
                )

                if (
                    result.size.width <= maxWidthPx &&
                    !result.hasVisualOverflow
                ) {
                    break
                }

                size = (size.value - 2).sp
            }

            size
        }

        Text(
            text = text,
            modifier = modifier,
            fontSize = fontSize,
            maxLines = maxLines,
            textAlign = textAlign
        )
    }
}