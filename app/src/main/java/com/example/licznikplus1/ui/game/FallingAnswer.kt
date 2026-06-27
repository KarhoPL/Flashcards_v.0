
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import com.example.licznikplus1.ui.game.AnswerChip

@Composable
fun FallingWord(
    text: String,
    screenWidth: Int,
    screenHeight: Int,
    onClick: () -> Unit,
    onFinished: () -> Unit,
    roundId: Long
){
    val answerWidthPx = (screenWidth * 0.55f).toInt()

    val x = remember(screenWidth) {
        (32..(screenWidth - answerWidthPx - 32)).random()
    }
    val answerWidth = with(LocalDensity.current) {
        answerWidthPx.toDp()
    }

    val y = remember {
        Animatable(8f)
    }

    LaunchedEffect(Unit) {

        y.animateTo(
            targetValue = screenHeight.toFloat() + 150f,
            animationSpec = tween(
                durationMillis = 5000,
                easing = LinearEasing
            )
        )
        onFinished()
    }

    AnswerChip(
        text = text,
        width = answerWidth,
        modifier = Modifier
            .offset {
                IntOffset(
                    x,
                    y.value.toInt()
                )
            }
            .clickable {
                onClick()
            }
    )
}