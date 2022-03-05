package com.github.pwoicik.uekschedule.feature_schedule.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    isSpinning: Boolean = true
) {
    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        color = backgroundColor,
        shape = RoundedCornerShape(50f),
        modifier = modifier.progressSemantics()
    ) {
        val spin = remember { Animatable(0f) }

        LaunchedEffect(isSpinning) {
            if (isSpinning) {
                spin.animateTo(
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(tween(
                        durationMillis = 1000,
                        easing = LinearEasing
                    ))
                )
            } else {
                spin.stop()
            }
        }

        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            tint = color,
            modifier = Modifier
                .padding(6.dp)
                .rotate(spin.value)
        )
    }
}
