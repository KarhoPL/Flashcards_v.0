package com.example.licznikplus1.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TextToSpeechManager(context: Context) {

    private lateinit var textToSpeech: TextToSpeech

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.CHINESE
            }
        }
    }

    fun speak(text: String) {
        textToSpeech.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
    }

    fun shutdown() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}