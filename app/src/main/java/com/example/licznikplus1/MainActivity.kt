package com.example.licznikplus1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CounterScreen()
        }
    }
}

enum class FlashcardField {
    POLISH,
    PINYIN,
    CHINESE
}

data class Flashcard(
    val polish: String,
    val pinyin: String,
    val chinese: String
)

fun Flashcard.getField(field: FlashcardField): String {
    return when (field) {
        FlashcardField.POLISH -> polish
        FlashcardField.PINYIN -> pinyin
        FlashcardField.CHINESE -> chinese
    }
}

@Composable

fun CounterScreen() {

    val flashcards = listOf(
        Flashcard("dzień dobry", "nǐ hǎo", "你好"),
        Flashcard("dziękuję", "xièxie", "谢谢"),
        Flashcard("do widzenia", "zàijiàn", "再见"),
        Flashcard("woda", "shuǐ", "水"),
        Flashcard("herbata", "chá", "茶")
    )

    var currentIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val currentCard = flashcards[currentIndex]
    var showAnswer by rememberSaveable {
        mutableStateOf(false)
    }
    var questionField by rememberSaveable {
        mutableStateOf(FlashcardField.CHINESE)
    }
    val answerFields = FlashcardField.values().filter {
        it != questionField
    }
    Column(
        modifier = Modifier.fillMaxSize(),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal =30.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,


        ) {
            Button(
                onClick = {
                    currentIndex = (currentIndex - 1 + flashcards.size) % flashcards.size
                    showAnswer = false

                }
            ) {
                Text("Poprzednie")
            }
            //Spacer(modifier = Modifier.size())
            Button(
                onClick = {
                    currentIndex = (currentIndex + 1) % flashcards.size
                    showAnswer = false

                }
            ) {
                Text("Następne")
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    questionField = FlashcardField.CHINESE
                    showAnswer = false
                }
            ) {
                if (questionField == FlashcardField.CHINESE)
                    Text("✓中文")
                else
                    Text("中文")
            }

            // to będzie kiedyś do zastąpienia dźwiękiem
            Button(
                onClick = {
                    questionField = FlashcardField.PINYIN
                    showAnswer = false
                }
            ) {
                if (questionField == FlashcardField.PINYIN)
                    Text("✓Pinyin")
                else
                    Text("Pinyin")
            }

            Button(
                onClick = {
                    questionField = FlashcardField.POLISH
                    showAnswer = false
                }
            ) {
                if (questionField == FlashcardField.POLISH)
                    Text("✓PL")
                else
                    Text("PL")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    showAnswer = true
                }
            ) {
                Text("Pokaż odpowiedź")
            }
        }

    }


    }



@Preview(showBackground = true)
@Composable
fun CounterPreview() {
    CounterScreen()
}