package data

data class FlashcardProgress(
    val cardId: String,

    val nextReviewDate: Long = 0L,
    val intervalDays: Int = 0,

    val repetitions: Int = 0,
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0,

    val lastReviewedAt: Long? = null,
    val firstLearnedAt: Long? = null,

    val consecutiveCorrect: Int = 0
)
