package com.example.licznikplus1.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TextToSpeechManager(context: Context) {

    private lateinit var textToSpeech: TextToSpeech

    private var isReady = false

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.CHINESE
                isReady = true
            }
        }
    }

    fun speak(text: String) {

        if (!isReady) {
            return
        }

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