package com.example.licznikplus1.ui.game

import FallingWord
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import com.example.licznikplus1.data.FallingAnswer

@Composable
fun FallingAnswersLayer(
    visibleAnswers: SnapshotStateList<FallingAnswer>,
    screenWidth: Int,
    screenHeight: Int,
    onAnswerClick: (FallingAnswer) -> Unit,
    onAnswerFinished: (FallingAnswer) -> Unit,
    fallDuration: Int
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
    ) {

        visibleAnswers.forEachIndexed { index, answer ->
            key(answer.id) {

                val answerWidth = (screenWidth * 0.55f).toInt()

                val x = remember(answer.id) {
                    (32..(screenWidth - answerWidth - 32)).random()
                }

                FallingWord(
                    text = answer.text,
                    x = x,
                    startY = -200f,
                    endY = screenHeight.toFloat() + 150f,
                    startDelay = index * 700L,
                    onClick = {
                        onAnswerClick(answer)
                    },
                    isCorrect = answer.isCorrect,
                    onFinished = {
                        onAnswerFinished(answer)
                    },
                    durationMillis = fallDuration
                )
            }
        }
    }
}