package com.example.licznikplus1.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.licznikplus1.audio.TextToSpeechManager
import com.example.licznikplus1.data.DatabaseProvider
import com.example.licznikplus1.data.Flashcard
import com.example.licznikplus1.data.FlashcardField
import com.example.licznikplus1.data.FlashcardRepository
import com.example.licznikplus1.data.GameBuilder
import com.example.licznikplus1.data.GamePreferences
import com.example.licznikplus1.data.getField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@Composable

fun GameScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val textToSpeechManager = remember {
        TextToSpeechManager(context)
    }
    val coroutineScope = rememberCoroutineScope()

    val flashcardRepository = remember {
        FlashcardRepository(context)
    }

    val flashcards = remember {
        mutableStateListOf<Flashcard>()
    }
    val database = remember {
        DatabaseProvider.getDatabase(context)
    }
    val gamePreferences = remember {
        GamePreferences(context)
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
    val fallDuration = maxOf(
        1800,
        6000 - score * 50
    )
    val highScore by gamePreferences.highScore.collectAsState(
        initial = 0
    )
    var lives by remember {
        mutableStateOf(3)
    }

    var gameOver by remember {
        mutableStateOf(false)
    }

    fun nextQuestion() {

        if (gameOver) {
            return

        }

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

        flashcards.clear()
        flashcards.addAll(
            flashcardRepository.getAll()
        )

        learnedCards.clear()
        learnedCards.addAll(
            GameBuilder.getLearnedCards(
                flashcards = flashcards,
                flashcardProgressDao = flashcardProgressDao
            )
        )

        nextQuestion()
    }
    if (flashcards.isEmpty()) {
        return
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
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

            GamePlayArea(
                question = questionText,
                questionFontSize = 48.sp,
                visibleAnswers = visibleAnswers,
                onAnswerClick = { answer ->

                    if (answer.isCorrect) {

                        score++

                        if (score > highScore) {

                            coroutineScope.launch {

                                gamePreferences.saveHighScore(score)
                            }
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
                },
                fallDuration = fallDuration
            )

        }
    }
    if (gameOver) {

        GameOverDialog(
            score = score,
            highScore = highScore,
            onRestart = {
                score = 0
                lives = 3
                gameOver = false
                nextQuestion()
            },
            onBackToMenu = {
                navController.popBackStack()
            }
        )
    }
}