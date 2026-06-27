package com.example.licznikplus1.ui.game

import FallingWord
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import com.example.licznikplus1.data.FallingAnswer

@Composable
fun FallingAnswersLayer(
    visibleAnswers: SnapshotStateList<FallingAnswer>,
    roundId: Long,
    screenWidth: Int,
    screenHeight: Int,
    onAnswerClick: (FallingAnswer) -> Unit,
    onAnswerFinished: (FallingAnswer) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
    ) {

        visibleAnswers.forEachIndexed { index, answer ->
            key(answer.id) {

                FallingWord(
                    text = answer.text,
                    screenWidth = screenWidth,
                    screenHeight = screenHeight,
                    startDelay = index * 700L,
                    roundId = roundId,
                    onClick = {
                        onAnswerClick(answer)
                    },
                    onFinished = {
                        onAnswerFinished(answer)
                    }
                )
            }
        }
    }
}