package com.github.pwoicik.uekschedule.screen.scheduleScreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.database.Class
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@Composable
fun ScheduleColumnItemLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val leftCol = measurables[0].measure(constraints.copy(minWidth = 0))
        val rightCol = measurables[2].run {
            val width = minIntrinsicWidth(Int.MAX_VALUE)
            measure(
                constraints.copy(
                    minWidth = width,
                    maxWidth = width
                )
            )
        }

        val midColWidth = constraints.maxWidth - leftCol.width - rightCol.width
        val midCol = measurables[1].measure(
            constraints.copy(
                minWidth = midColWidth,
                maxWidth = midColWidth
            )
        )

        layout(constraints.maxWidth, midCol.height) {
            leftCol.placeRelative(0, 0)
            midCol.placeRelative(leftCol.width, 0)
            rightCol.placeRelative(leftCol.width + midCol.width, 0)
        }
    }
}

@Composable
fun ScheduleColumnItem(clazz: Class, timeNow: ZonedDateTime) {
    val timeDifference = timeNow.until(clazz.startDateTime, ChronoUnit.MINUTES)
    val status = when {
        timeNow.isBefore(clazz.startDateTime) -> ClassStatus.NOT_STARTED
        timeNow.isBefore(clazz.endDateTime) -> ClassStatus.IN_PROGRESS
        else -> ClassStatus.ENDED
    }

    ScheduleColumnItemLayout(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .alpha(if (status == ClassStatus.ENDED) ContentAlpha.medium else ContentAlpha.high)
    ) {
        ClassTimeColumn(clazz)
        ClassDetailsColumn(clazz)
        ClassStatusColumn(status, timeDifference, timeNow, clazz)
    }
}
