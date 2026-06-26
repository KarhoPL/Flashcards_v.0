package com.example.licznikplus1.data

class SessionBuilder {

    companion object {

        suspend fun buildTestingSession(
            flashcards: List<Flashcard>,
            flashcardProgressDao: FlashcardProgressDao,
            newCardsPerDay: Int,
            askChinese: Boolean,
            askPolish: Boolean,
            askAudio: Boolean
        ): List<SessionCard> {

            val now = System.currentTimeMillis()

            val dueCards = flashcardProgressDao
                .getDueCards(now)
                .map {
                    SessionCard(
                        cardId = it.cardId,
                        questionField = it.questionField
                    )
                }

            val introducedToday = flashcardProgressDao
                .getIntroducedToday(startOfToday())

            val introducedTodayCount = introducedToday
                .map { it.cardId }
                .distinct()
                .size

            val remainingNewCards =
                (newCardsPerDay - introducedTodayCount).coerceAtLeast(0)

            val introducedProgress =
                flashcardProgressDao.getAll()

            val introducedPairs = introducedProgress.map {
                it.cardId to it.questionField
            }.toSet()

            val newCards = mutableListOf<SessionCard>()

            for (flashcard in flashcards) {

                val cardsForWord = mutableListOf<SessionCard>()

                if (
                    askChinese &&
                    (flashcard.id to FlashcardField.CHINESE) !in introducedPairs
                ) {
                    cardsForWord.add(
                        SessionCard(
                            cardId = flashcard.id,
                            questionField = FlashcardField.CHINESE
                        )
                    )
                }

                if (
                    askPolish &&
                    (flashcard.id to FlashcardField.POLISH) !in introducedPairs
                ) {
                    cardsForWord.add(
                        SessionCard(
                            cardId = flashcard.id,
                            questionField = FlashcardField.POLISH
                        )
                    )
                }

                if (
                    askAudio &&
                    (flashcard.id to FlashcardField.AUDIO) !in introducedPairs
                ) {
                    cardsForWord.add(
                        SessionCard(
                            cardId = flashcard.id,
                            questionField = FlashcardField.AUDIO
                        )
                    )
                }

                if (cardsForWord.isNotEmpty()) {
                    newCards.addAll(cardsForWord)
                }

                if (newCards.map { it.cardId }.distinct().size >= remainingNewCards) {
                    break
                }
            }


            return (dueCards + newCards).shuffled()
        }

        suspend fun buildLearningSession(
            flashcardProgressDao: FlashcardProgressDao,
            learningCardsCount: Int,
            askChinese: Boolean,
            askPolish: Boolean,
            askAudio: Boolean
        ): List<SessionCard> {

            return flashcardProgressDao
                .getOrderedByNextReviewDate()
                .filter {
                    when (it.questionField) {
                        FlashcardField.CHINESE -> askChinese
                        FlashcardField.POLISH -> askPolish
                        FlashcardField.AUDIO -> askAudio
                        FlashcardField.PINYIN -> false
                    }
                }
                .take(learningCardsCount)
                .map {
                    SessionCard(
                        cardId = it.cardId,
                        questionField = it.questionField
                    )
                }
        }
    }
}

private fun startOfToday(): Long {
    return java.time.LocalDate.now()
        .atStartOfDay(java.time.ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}