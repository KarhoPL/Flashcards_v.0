
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import com.example.licznikplus1.ui.game.AnswerChip
import kotlinx.coroutines.delay

@Composable
fun FallingWord(
    text: String,
    screenWidth: Int,
    screenHeight: Int,
    startDelay: Long,
    onClick: () -> Unit,
    onFinished: () -> Unit,
    roundId: Long
){
    val answerWidthPx = (screenWidth * 0.55f).toInt()

        val x = remember(screenWidth) {
            (32..(screenWidth - answerWidthPx - 32)).random()
        }

    val y = remember {
        Animatable(-200f)
    }

    LaunchedEffect(Unit) {
        delay(startDelay)
        y.animateTo(
            targetValue = screenHeight.toFloat() + 150f,
            animationSpec = tween(
                durationMillis = 8000,
                easing = LinearEasing
            )
        )
        onFinished()
    }

    AnswerChip(
        text = text,
        modifier = Modifier
            .offset {
                IntOffset(
                    x,
                    y.value.toInt()
                )
            }
            .clickable {
                println("CLICK")
                onClick()
            }
    )
}