package com.example.licznikplus1.ui.game

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import com.example.licznikplus1.data.FallingAnswer

@Composable
fun GamePlayArea(
    question: String,
    questionFontSize: TextUnit,
    visibleAnswers: SnapshotStateList<FallingAnswer>,
    onAnswerClick: (FallingAnswer) -> Unit,
    onAnswerFinished: (FallingAnswer) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        QuestionCard(
            text = question,
            maxFontSize = questionFontSize
        )

        BoxWithConstraints(
            modifier = Modifier.weight(1f)
        ) {

            val density = LocalDensity.current

            val widthPx = with(density) {
                maxWidth.roundToPx()
            }

            val heightPx = with(density) {
                maxHeight.roundToPx()
            }

            FallingAnswersLayer(
                visibleAnswers = visibleAnswers,
                screenWidth = widthPx,
                screenHeight = heightPx,
                onAnswerClick = onAnswerClick,
                onAnswerFinished = onAnswerFinished
            )
        }
    }
}