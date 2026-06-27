package com.example.licznikplus1.data

data class FallingAnswer(
    val id: Long,
    val text: String,
    val isCorrect: Boolean,
    val delayMillis: Int
)
data class GameQuestion(
    val flashcard: Flashcard,
    val questionField: FlashcardField
)

class GameBuilder {

    companion object {

        suspend fun getLearnedCards(
            flashcards: List<Flashcard>,
            flashcardProgressDao: FlashcardProgressDao
        ): List<GameQuestion> {

            return flashcardProgressDao
                .getAll()
                .mapNotNull { progress ->

                    val flashcard = flashcards.find {
                        it.id == progress.cardId
                    } ?: return@mapNotNull null

                    GameQuestion(
                        flashcard = flashcard,
                        questionField = progress.questionField
                    )
                }
        }
        fun buildAnswers(
            question: GameQuestion,
            flashcards: List<Flashcard>
        ): List<FallingAnswer> {

            val correctAnswer = when (question.questionField) {
                FlashcardField.CHINESE,
                FlashcardField.AUDIO -> question.flashcard.polish

                FlashcardField.POLISH -> question.flashcard.chinese

                FlashcardField.PINYIN -> question.flashcard.chinese
            }

            val wrongAnswers = flashcards
                .filter { it.id != question.flashcard.id }
                .shuffled()
                .take(2)
                .map {
                    when (question.questionField) {
                        FlashcardField.CHINESE,
                        FlashcardField.AUDIO -> it.polish

                        FlashcardField.POLISH -> it.chinese

                        FlashcardField.PINYIN -> it.chinese
                    }
                }

            return (wrongAnswers + correctAnswer)
                .shuffled()
                .mapIndexed { index, answer ->

                FallingAnswer(
                    id = System.nanoTime() + index,
                    text = answer,
                    isCorrect = answer == correctAnswer,
                    delayMillis = index * 700
                )

            }

                }
        fun getRandomQuestion(
            learnedCards: List<GameQuestion>
        ): GameQuestion? {

            if (learnedCards.isEmpty()) {
                return null
            }

            return learnedCards.random()
        }
    }

}
