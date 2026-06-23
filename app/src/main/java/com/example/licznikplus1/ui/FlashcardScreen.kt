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
import com.example.licznikplus1.data.ReviewEvent
import com.example.licznikplus1.data.ReviewEventEntity
import com.example.licznikplus1.data.ReviewRating
import com.example.licznikplus1.data.SessionMode
import com.example.licznikplus1.data.getField
import com.example.licznikplus1.data.hasAudio
import com.example.licznikplus1.data.loadFlashcards
import com.example.licznikplus1.ui.components.NavigationButtons
import kotlinx.coroutines.launch

@Composable

fun FlashcardScreen(
    navController: NavController,
    questionField: FlashcardField,
    autoPlayAudio: Boolean,
    sessionMode: SessionMode
){
    val context = LocalContext.current
    val database = remember {
        DatabaseProvider.getDatabase(context)
    }
    val reviewEventDao = remember {
        database.reviewEventDao()
    }

    val coroutineScope = rememberCoroutineScope()
    val textToSpeechManager = remember {
        TextToSpeechManager(context)
    }
    val flashcards = loadFlashcards(context)

    val sessionQueue = remember {
        mutableStateListOf<Int>().apply {
            addAll(flashcards.indices)
        }
    }
    val reviewEvents = remember {
        mutableStateListOf<ReviewEvent>()
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

    val currentCard = flashcards[sessionQueue.first()]
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

        val event = ReviewEventEntity(
            cardId = currentCard.id,
            timestamp = System.currentTimeMillis(),
            rating = rating
        )

        if (sessionMode == SessionMode.TESTING) {

            reviewEvents.add(
                ReviewEvent(
                    cardId = event.cardId,
                    timestamp = event.timestamp,
                    rating = event.rating
                )
            )
        }

        val answeredCardIndex = sessionQueue.removeAt(0)

        if (rating == ReviewRating.AGAIN) {
            sessionQueue.add(answeredCardIndex)
        }

        showAnswer = false

        if (sessionMode == SessionMode.TESTING) {

            coroutineScope.launch {
                reviewEventDao.insert(event)
                reviewCount = reviewEventDao.getCount()
            }
        }
    }

    fun playAudio() {
        textToSpeechManager.speak(currentCard.chinese)
    }
    LaunchedEffect(sessionQueue.firstOrNull(), questionField) {
        if (questionField == FlashcardField.AUDIO && sessionQueue.isNotEmpty()) {
            playAudio()
        }
    }


    val answerFields = FlashcardField.values().filter {
        it != questionField
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

                    if (autoPlayAudio && questionField.hasAudio()) {
                        playAudio()
                    }
                } else {
                    if (questionField.hasAudio()) {
                        playAudio()
                    }
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
                    text = currentCard.getField(questionField),
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