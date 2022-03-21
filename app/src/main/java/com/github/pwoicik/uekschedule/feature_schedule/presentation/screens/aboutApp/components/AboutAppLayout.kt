package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.aboutApp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Section(
    title: String,
    text: AnnotatedString,
    inlineContent: Map<String, InlineTextContent> = mapOf()
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        SectionTitle(text = title)
        Text(
            text = text,
            inlineContent = inlineContent
        )
    }
}

@Composable
fun SectionTitle(text: String) {
    SectionTitleLayout {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
        )
    }
}

@Composable
private fun SectionTitleLayout(
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
    ) { measurables, constraints ->
        val title = measurables[0].measure(constraints)

        val dividerWidth = (title.width * 1.4).toInt().coerceAtMost(constraints.maxWidth)
        val divider = measurables[1].measure(constraints.copy(minWidth = dividerWidth))

        layout(constraints.maxWidth, title.height + divider.height) {
            title.placeRelative(0, 0)
            divider.placeRelative(title.width / 3, title.height)
        }
    }
}
