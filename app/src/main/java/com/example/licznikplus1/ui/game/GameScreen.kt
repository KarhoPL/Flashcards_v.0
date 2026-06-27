package com.example.licznikplus1.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    val coroutineScope = rememberCoroutineScope()

    val flashcards = remember {
        loadFlashcards(context)
    }
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

            val start = System.currentTimeMillis()

            answers = GameBuilder.buildAnswers(
                question = it,
                flashcards = flashcards
            )

            println("Build answers: ${System.currentTimeMillis() - start} ms")

            visibleAnswers.clear()

            visibleAnswers.clear()
            visibleAnswers.addAll(answers)

            if (it.questionField == FlashcardField.AUDIO) {
                println("AUDIO: ${it.flashcard.chinese}")

                coroutineScope.launch {
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

        FallingAnswersLayer(
            visibleAnswers = visibleAnswers,
            roundId = roundId,
            screenWidth = screenWidth,
            screenHeight = screenHeight,

            onAnswerClick = { answer ->

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

            onAnswerFinished = { answer ->

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