package com.github.pwoicik.uekschedule.screen.scheduleScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.pwoicik.uekschedule.model.Class
import com.github.pwoicik.uekschedule.ui.theme.UEKScheduleTheme
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreen() {
    val viewModel = viewModel(modelClass = ScheduleScreenVieModel::class.java)
    val schedule = viewModel.getSchedule("184261")

    val classes = schedule.value.classes
    if (!classes.isNullOrEmpty()) {
        LazyColumn {
            stickyHeader {
                ScheduleColumnStickyHeader(classes[0].date)
            }
            item {
                ScheduleColumnItem(clazz = classes[0])
                Divider(modifier = Modifier.height(1.dp))
            }

            for (i in 1..classes.lastIndex) {
                val prevClass = classes[i - 1]
                val nextClass = classes[i]

                if (prevClass.date != nextClass.date) {
                    stickyHeader {
                        ScheduleColumnStickyHeader(nextClass.date)
                    }
                }

                item {
                    ScheduleColumnItem(clazz = nextClass)
                    Divider(modifier = Modifier.height(1.dp))
                }
            }
        }
    }
}

@Composable
fun ScheduleColumnStickyHeader(date: String) {
    Surface(
        color = Color.LightGray,
        elevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            date,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun ScheduleColumnItemLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val leftCol = measurables[0].run {
            val width = minIntrinsicWidth(Int.MAX_VALUE)
            measure(
                constraints.copy(
                    minWidth = width,
                    maxWidth = width
                )
            )
        }

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
fun ScheduleColumnItem(clazz: Class) {
    val timeNow = ZonedDateTime.now()
    val timeDifference = timeNow.until(clazz.startDate, ChronoUnit.MINUTES)
    val status = when {
        timeNow.isBefore(clazz.startDate) -> ClassStatus.NOT_STARTED
        timeNow.isBefore(clazz.endDate) -> ClassStatus.IN_PROGRESS
        else -> ClassStatus.ENDED
    }

    ScheduleColumnItemLayout(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .alpha(if (status == ClassStatus.ENDED) ContentAlpha.medium else ContentAlpha.high)
    ) {
        Column {
            Text(clazz.startTime, modifier = Modifier.padding(bottom = 4.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(clazz.endTime)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(clazz.subject, modifier = Modifier.padding(bottom = 8.dp))

            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.body2,
                LocalContentAlpha provides ContentAlpha.medium,
            ) {
                Text(clazz.teacher, modifier = Modifier.padding(bottom = 4.dp))
                if (clazz.details == null) {
                    Text(clazz.type, modifier = Modifier.padding(bottom = 4.dp))
                } else {
                    CompositionLocalProvider(LocalContentColor provides Color.Red) {
                        Text(clazz.type)
                        Text(clazz.details!!, modifier = Modifier.padding(bottom = 4.dp))
                    }
                }
                if (clazz.location != null) {
                    Text(
                        clazz.location!!,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Column {
            val text = when(status) {
                ClassStatus.NOT_STARTED -> "ZA " +
                        if (timeDifference < 60) "${timeDifference}min"
                        else "${timeDifference / 60}h"
                ClassStatus.IN_PROGRESS ->
                    "TRWA JESZCZE ${timeNow.until(clazz.endDate, ChronoUnit.MINUTES)}min"
                ClassStatus.ENDED ->
                    "MINĘŁO"
            }

            Text(
                text,
                textAlign = TextAlign.Right,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.width(IntrinsicSize.Min)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleColumnItemPreview() {
    UEKScheduleTheme {
        val c = Class(
            subject = "Administrowanie sieciami komputerowymi",
            teacher = "mgr Jakub Kanclerz",
            date = "2021-11-17",
            startTime = "09:45",
            endTime = "11:15",
            location = "Paw. A 014 lab. Win 8.1, Office16",
            type = "ćwiczenia"
        )
        ScheduleColumnItem(clazz = c)
    }
}

enum class ClassStatus {
    NOT_STARTED,
    IN_PROGRESS,
    ENDED,
}
