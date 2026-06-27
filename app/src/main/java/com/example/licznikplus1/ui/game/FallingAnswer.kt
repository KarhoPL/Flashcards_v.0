
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import com.example.licznikplus1.ui.game.AnswerChip
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FallingWord(
    text: String,
    x: Int,
    startY: Float,
    endY: Float,
    startDelay: Long,
    isCorrect: Boolean,
    onClick: () -> Unit,
    onFinished: () -> Unit
){
    val y = remember {
        Animatable(startY)
    }
    var wrongAnswer by remember {
        mutableStateOf(false)
    }

    val cardColor by animateColorAsState(
        targetValue =
        if (wrongAnswer)
            Color(0xFFFFCDD2)
        else
            MaterialTheme.colorScheme.surface,
        label = ""
    )
    LaunchedEffect(Unit) {
        delay(startDelay)
        y.animateTo(
            targetValue = endY,
            animationSpec = tween(
                durationMillis = 4500,
                easing = LinearEasing
            )
        )
        onFinished()
    }

    AnswerChip(
        text = text,
        containerColor = cardColor,
        modifier = Modifier
            .offset {
                IntOffset(
                    x,
                    y.value.toInt()
                )
            }
            .clickable {

                if (isCorrect) {
                    onClick()
                } else {

                    wrongAnswer = true

                    kotlinx.coroutines.CoroutineScope(
                        kotlinx.coroutines.Dispatchers.Main
                    ).launch {

                        delay(250)

                        onClick()
                    }
                }
            }
    )
}