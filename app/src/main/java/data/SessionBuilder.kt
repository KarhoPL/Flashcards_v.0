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
            val flashcardsById = flashcards.associateBy { it.id }
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
            val incompleteCardIds = introducedProgress
                .groupBy { it.cardId }
                .filter { (_, progress) ->

                    val learnedDirections = progress.map {
                        it.questionField
                    }.toSet()

                    val enabledDirections = buildSet {

                        if (askChinese) add(FlashcardField.CHINESE)
                        if (askPolish) add(FlashcardField.POLISH)
                        if (askAudio) add(FlashcardField.AUDIO)

                    }

                    learnedDirections.isNotEmpty() &&
                            learnedDirections.size < enabledDirections.size
                }
                .keys

            val newCards = mutableListOf<SessionCard>()
            val usedCardIds = mutableSetOf<String>()

            for (cardId in incompleteCardIds) {

                val flashcard = flashcardsById[cardId] ?: continue

                val cards = buildMissingDirections(
                    flashcard = flashcard,
                    introducedPairs = introducedPairs,
                    askChinese = askChinese,
                    askPolish = askPolish,
                    askAudio = askAudio
                )

                if (cards.isNotEmpty()) {

                    newCards.addAll(cards)
                    usedCardIds.add(cardId)

                }

                if (usedCardIds.size >= remainingNewCards) {
                    break
                }
            }
            for (flashcard in flashcards) {

                if (flashcard.id in usedCardIds) {
                    continue
                }
                val cardsForWord = buildMissingDirections(
                    flashcard = flashcard,
                    introducedPairs = introducedPairs,
                    askChinese = askChinese,
                    askPolish = askPolish,
                    askAudio = askAudio
                )

                if (cardsForWord.isNotEmpty()) {
                    newCards.addAll(cardsForWord)
                    usedCardIds.add(flashcard.id)
                }


                if (usedCardIds.size >= remainingNewCards) {
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
        private fun buildMissingDirections(
            flashcard: Flashcard,
            introducedPairs: Set<Pair<String, FlashcardField>>,
            askChinese: Boolean,
            askPolish: Boolean,
            askAudio: Boolean
        ): List<SessionCard> {

            val result = mutableListOf<SessionCard>()

            if (
                askChinese &&
                (flashcard.id to FlashcardField.CHINESE) !in introducedPairs
            ) {
                result.add(
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
                result.add(
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
                result.add(
                    SessionCard(
                        cardId = flashcard.id,
                        questionField = FlashcardField.AUDIO
                    )
                )
            }

            return result
        }
    }
}

private fun startOfToday(): Long {
    return java.time.LocalDate.now()
        .atStartOfDay(java.time.ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}