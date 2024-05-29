package uekschedule.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun Spinner(modifier: Modifier = Modifier) {
    val color1 = MaterialTheme.colorScheme.onSurface
    val color2 = MaterialTheme.colorScheme.tertiary
    val transition = rememberInfiniteTransition(label = "spinnin'")
    val startAngle1 by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1_400, easing = LinearEasing),
        ),
        label = "startAngle1",
    )
    val startAngle2 by transition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1_200, easing = LinearEasing),
        ),
        label = "startAngle2",
    )
    Canvas(modifier = modifier.size(48.dp)) {
        val width = 5.dp.toPx()
        val offset1 = width / 2
        val size1 = size.width - width
        drawArc(
            color = color1,
            startAngle = startAngle1,
            sweepAngle = 180f,
            useCenter = false,
            style = Stroke(width),
            topLeft = Offset(offset1, offset1),
            size = Size(size1, size1),
        )

        val gap = 4.dp.toPx()
        val offset2 = offset1 + width + gap
        val size2 = size1 - offset2 - width / 2 - gap
        drawArc(
            color = color2,
            startAngle = startAngle2,
            sweepAngle = 180f,
            useCenter = false,
            style = Stroke(width),
            topLeft = Offset(offset2, offset2),
            size = Size(size2, size2),
        )
    }
}
