package com.example.licznikplus1.ui
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.licznikplus1.audio.TextToSpeechManager
import com.example.licznikplus1.data.DatabaseProvider
import com.example.licznikplus1.data.FlashcardField
import com.example.licznikplus1.data.FlashcardProgressEntity
import com.example.licznikplus1.data.ReviewEvent
import com.example.licznikplus1.data.ReviewEventEntity
import com.example.licznikplus1.data.ReviewRating
import com.example.licznikplus1.data.SessionBuilder
import com.example.licznikplus1.data.SessionCard
import com.example.licznikplus1.data.SessionMode
import com.example.licznikplus1.data.getField
import com.example.licznikplus1.data.loadFlashcards
import com.example.licznikplus1.ui.components.NavigationButtons
import kotlinx.coroutines.launch


@Composable

fun FlashcardScreen(
    navController: NavController,
    autoPlayAudio: Boolean,
    sessionMode: SessionMode,
    newCardsPerDay: Int,
    learningCardsCount: Int,
    askChinese: Boolean,
    askPolish: Boolean,
    askAudio: Boolean,
){
    val context = LocalContext.current
    val database = remember {
        DatabaseProvider.getDatabase(context)
    }
    val reviewEventDao = remember {
        database.reviewEventDao()
    }
    val flashcardProgressDao = remember {
        database.flashcardProgressDao()
    }
    val coroutineScope = rememberCoroutineScope()
    val textToSpeechManager = remember {
        TextToSpeechManager(context)
    }
    val flashcards = loadFlashcards(context)

    val sessionQueue = remember {
        mutableStateListOf<SessionCard>()
    }

    val cardsFailedThisSession = remember {
        mutableSetOf<String>()
    }
    val lastRatingForCard = remember {
        mutableMapOf<String, ReviewRating>()
    }
    val reviewEvents = remember {
        mutableStateListOf<ReviewEvent>()
    }
    LaunchedEffect(sessionMode) {

        if (sessionMode == SessionMode.LEARNING) {

            sessionQueue.clear()

            sessionQueue.addAll(
                SessionBuilder.buildLearningSession(
                    flashcardProgressDao = flashcardProgressDao,
                    learningCardsCount = learningCardsCount,
                    askChinese = askChinese,
                    askPolish = askPolish,
                    askAudio = askAudio
                )
            )
        } else if (sessionMode == SessionMode.TESTING) {
            sessionQueue.clear()
            sessionQueue.addAll(
                SessionBuilder.buildTestingSession(
                    flashcards = flashcards,
                    flashcardProgressDao = flashcardProgressDao,
                    newCardsPerDay = newCardsPerDay,
                    askChinese = askChinese,
                    askPolish = askPolish,
                    askAudio = askAudio
                )
            )
        }
    }

    if (sessionQueue.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Sesja zakończona")
        }
        return
    }

    val currentSessionCard = sessionQueue.first()

    val currentCard = flashcards.first {
        it.id == currentSessionCard.cardId
    }
    val currentQuestionField = currentSessionCard.questionField
    var showAnswer by rememberSaveable {
        mutableStateOf(false)
    }
    var reviewCount by rememberSaveable {
        mutableStateOf(0)
    }
    LaunchedEffect(Unit) {
        reviewCount = reviewEventDao.getCount()
    }

    fun answer(rating: ReviewRating) {

        if (rating == ReviewRating.AGAIN) {
            cardsFailedThisSession.add(currentCard.id)
        }

        val event = ReviewEventEntity(
            cardId = currentCard.id,
            questionField  = currentQuestionField,
            timestamp = System.currentTimeMillis(),
            rating = rating
        )
        lastRatingForCard[currentCard.id] = rating
        if (sessionMode == SessionMode.TESTING) {

            reviewEvents.add(
                ReviewEvent(
                    cardId = event.cardId,
                    timestamp = event.timestamp,
                    rating = event.rating
                )
            )
        }

        val answeredCard = sessionQueue.removeAt(0)

        if (rating == ReviewRating.AGAIN) {
            sessionQueue.add(answeredCard)
        }

        showAnswer = false

        if (sessionMode == SessionMode.TESTING) {

            coroutineScope.launch {

                reviewEventDao.insert(event)

                val progress =
                    flashcardProgressDao.getByCardId(
                        cardId = currentCard.id,
                        questionField  = currentQuestionField
                    ) ?: FlashcardProgressEntity(
                        cardId = currentCard.id,
                        questionField  = currentQuestionField,
                        introducedAt = System.currentTimeMillis()
                    )

                val now = System.currentTimeMillis()

                val newIntervalHours =
                    if (cardsFailedThisSession.contains(currentCard.id)) {
                        8
                    } else {
                        when (rating) {
                            ReviewRating.HARD -> 8
                            ReviewRating.GOOD -> 12
                            ReviewRating.EASY -> progress.intervalHours + 12
                            ReviewRating.AGAIN -> progress.intervalHours
                        }
                    }

                val updatedProgress = progress.copy(
                    intervalHours = newIntervalHours,
                    nextReviewDate = now + newIntervalHours * 60L * 60L * 1000L,
                    lastReviewedAt = now
                )

                flashcardProgressDao.insert(updatedProgress)

                reviewCount = reviewEventDao.getCount()
            }
        }
    }

    fun playAudio() {
        textToSpeechManager.speak(currentCard.chinese)
    }
    LaunchedEffect(sessionQueue.firstOrNull(), currentQuestionField) {
        if (currentQuestionField == FlashcardField.AUDIO && sessionQueue.isNotEmpty()) {
            playAudio()
        }
    }


    val answerFields = FlashcardField.values().filter {
        it != currentQuestionField
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (!showAnswer) {

                    showAnswer = true

                    // Automatically play pronunciation after revealing the answer.
                    if (autoPlayAudio) {
                        playAudio()
                    }

                } else {

                    // Replay pronunciation on demand.
                    playAudio()

                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.BottomCenter,

            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentCard.getField(currentQuestionField),
                    fontSize = 48.sp
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter,

            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                if (showAnswer) {

                    answerFields.forEach { field ->

                        Text(
                            text = currentCard.getField(field),
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NavigationButtons(
                onAgainClick = {
                    answer(ReviewRating.AGAIN)
                },
                onHardClick = {
                    answer(ReviewRating.HARD)
                },
                onGoodClick = {
                    answer(ReviewRating.GOOD)
                },
                onEasyClick = {
                    answer(ReviewRating.EASY)
                }
            )
        }
        Text("Źle: ${reviewEvents.count { it.rating == ReviewRating.AGAIN }}")

        Text("Trudne: ${reviewEvents.count { it.rating == ReviewRating.HARD }}")

        Text("Dobrze: ${reviewEvents.count { it.rating == ReviewRating.GOOD }}")

        Text("Łatwe: ${reviewEvents.count { it.rating == ReviewRating.EASY }}")

        Text("Odpowiedzi: $reviewCount")
    }


}

//@Preview(showBackground = true)
//@Composable
//fun FlashcardPreview() {
//    FlashcardScreen()
//}