package com.example.licznikplus1.ui.game

import FallingWord
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.licznikplus1.audio.TextToSpeechManager
import com.example.licznikplus1.data.DatabaseProvider
import com.example.licznikplus1.data.FlashcardField
import com.example.licznikplus1.data.GameBuilder
import com.example.licznikplus1.data.getField
import com.example.licznikplus1.data.loadFlashcards
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@Composable

fun GameScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidth = with(density) {
        configuration.screenWidthDp.dp.roundToPx()
    }

    val screenHeight = with(density) {
        configuration.screenHeightDp.dp.roundToPx()
    }
    val textToSpeechManager = remember {
        TextToSpeechManager(context)
    }

    val flashcards = loadFlashcards(context)

    val database = remember {
        DatabaseProvider.getDatabase(context)
    }

    val flashcardProgressDao = remember {
        database.flashcardProgressDao()
    }

    val learnedCards = remember {
        mutableStateListOf<com.example.licznikplus1.data.GameQuestion>()
    }
    var currentQuestion by remember {
        mutableStateOf<com.example.licznikplus1.data.GameQuestion?>(null)
    }
    var answers by remember {
        mutableStateOf<List<com.example.licznikplus1.data.FallingAnswer>>(emptyList())
    }
    val visibleAnswers = remember {
        mutableStateListOf<com.example.licznikplus1.data.FallingAnswer>()
    }
    var score by remember {
        mutableStateOf(0)
    }
    var highScore by remember {
        mutableStateOf(0)
    }
    var lives by remember {
        mutableStateOf(3)
    }

    var gameOver by remember {
        mutableStateOf(false)
    }
    var roundId by remember {
        mutableStateOf(0L)
    }
    fun nextQuestion() {

        if (gameOver) {
            return
        }
        roundId++

        currentQuestion =
            GameBuilder.getRandomQuestion(learnedCards)

        currentQuestion?.let {

            println(it.questionField)
            answers = GameBuilder.buildAnswers(
                question = it,
                flashcards = flashcards
            )
            visibleAnswers.clear()

            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {

                answers.forEach { answer ->

                    visibleAnswers.add(answer)

                    delay(700)
                }
            }
            if (it.questionField == FlashcardField.AUDIO) {
                println("AUDIO: ${it.flashcard.chinese}")

                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                    delay(300)
                    textToSpeechManager.speak(it.flashcard.chinese)
                }
            }
        }
    }

    LaunchedEffect(Unit) {

        learnedCards.clear()

        learnedCards.addAll(
            GameBuilder.getLearnedCards(
                flashcards = flashcards,
                flashcardProgressDao = flashcardProgressDao
            )
        )

        nextQuestion()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        GameHeader(
            lives = lives,
            score = score,
            highScore = highScore
        )
        val questionText = currentQuestion?.let {
            it.flashcard.getField(it.questionField)
        } ?: ""

        QuestionCard(
            text = questionText,
            maxFontSize =
            if (currentQuestion?.questionField == FlashcardField.CHINESE)
                64.sp
            else
                48.sp
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            val currentRound = roundId

            visibleAnswers.forEach { answer ->

                key(roundId, answer.id) {

                    FallingWord(
                        text = answer.text,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight,
                        roundId = roundId,
                        onClick = {

                            if (answer.isCorrect) {

                                score++

                                if (score > highScore) {
                                    highScore = score
                                }

                            } else {
                                lives--
                            }

                            if (lives <= 0) {
                                gameOver = true
                            } else {
                                nextQuestion()
                            }
                        },

                        onFinished = {

                            if (currentRound != roundId) {
                                return@FallingWord
                            }

                            if (answer.isCorrect) {

                                lives--

                                if (lives <= 0) {
                                    gameOver = true
                                } else {
                                    nextQuestion()
                                }
                            }
                        }
                    )
                }
            }
        }

    }
}