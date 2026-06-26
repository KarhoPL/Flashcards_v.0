package com.example.licznikplus1.data

class SessionBuilder {

    companion object {

        suspend fun buildTestingSession(
            flashcards: List<Flashcard>,
            flashcardProgressDao: FlashcardProgressDao,
            newCardsPerDay: Int
        ): List<SessionCard> {

            val now = System.currentTimeMillis()

            val dueCards = flashcardProgressDao
                .getDueCards(now)
                .map { SessionCard(it.cardId) }

            val introducedToday = flashcardProgressDao
                .getIntroducedToday(startOfToday())

            val remainingNewCards =
                (newCardsPerDay - introducedToday.size).coerceAtLeast(0)

            val introducedCardIds =
                flashcardProgressDao.getIntroducedCardIds().toSet()

            val newCards = flashcards
                .filter { it.id !in introducedCardIds }
                .take(remainingNewCards)
                .map { SessionCard(it.id) }

            return (dueCards + newCards)
                .distinctBy { it.cardId }
        }

        suspend fun buildLearningSession(
            flashcardProgressDao: FlashcardProgressDao,
            learningCardsCount: Int
        ): List<SessionCard> {

            return flashcardProgressDao
                .getOrderedByNextReviewDate()
                .take(learningCardsCount)
                .map { SessionCard(it.cardId) }
        }
    }
}

private fun startOfToday(): Long {
    return java.time.LocalDate.now()
        .atStartOfDay(java.time.ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}