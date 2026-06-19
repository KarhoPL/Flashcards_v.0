package com.example.licznikplus1.ui
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.licznikplus1.data.FlashcardField
import com.example.licznikplus1.data.getField
import com.example.licznikplus1.data.loadFlashcards
import com.example.licznikplus1.ui.components.NavigationButtons
import kotlin.random.Random
import androidx.compose.runtime.remember
import com.example.licznikplus1.audio.TextToSpeechManager
import com.example.licznikplus1.data.hasAudio



@Composable

fun FlashcardScreen(
    navController: NavController,
    questionField: FlashcardField,
    autoPlayAudio: Boolean
) {
    val context = LocalContext.current
    val textToSpeechManager = remember {
        TextToSpeechManager(context)
    }
    val flashcards = loadFlashcards(context)

    var currentIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val currentCard = flashcards[currentIndex]
    fun playAudio() {
        textToSpeechManager.speak(currentCard.chinese)
    }
    var showAnswer by rememberSaveable {
        mutableStateOf(false)
    }

    val answerFields = FlashcardField.values().filter {
        it != questionField
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    navController.navigate("settings")
                }
            ) {
                Text("⚙")
            }
        }

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

        NavigationButtons(
            onPreviousClick = {
                currentIndex = (currentIndex - 1 + flashcards.size) % flashcards.size
                showAnswer = false
            },
            onNextClick = {
                currentIndex = (currentIndex + 1) % flashcards.size
                showAnswer = false
            },
        )

    }


}



//@Preview(showBackground = true)
//@Composable
//fun FlashcardPreview() {
//    FlashcardScreen()
//}