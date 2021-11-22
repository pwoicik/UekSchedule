package com.github.pwoicik.uekschedule.screen.scheduleScreen

import android.app.Application
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.database.Class
import com.github.pwoicik.uekschedule.database.ScheduleViewModel
import com.github.pwoicik.uekschedule.database.ScheduleViewModelFactory
import com.github.pwoicik.uekschedule.ui.theme.UEKScheduleTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

@Composable
fun ScheduleScreen() {
    val viewModel: ScheduleViewModel = viewModel(
        factory = ScheduleViewModelFactory(LocalContext.current.applicationContext as Application)
    )

    val classes by viewModel.classes

    if (!classes.isNullOrEmpty()) {
        ScheduleClassesColumn(classes!!, viewModel)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScheduleClassesColumn(
    classes: List<Class>,
    viewModel: ScheduleViewModel
) {
    var timeNow by remember { mutableStateOf(ZonedDateTime.now()) }

    LaunchedEffect(key1 = timeNow) {
        delay(15_000)
        timeNow = ZonedDateTime.now()
    }

    val swipeRefreshState by viewModel.isRefreshing.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(swipeRefreshState),
        onRefresh = { viewModel.refresh() }
    ) {
        LazyColumn {
            stickyHeader {
                ScheduleColumnStickyHeader(classes[0].startDateTime.toLocalDate())
            }
            item(classes[0].id) {
                ScheduleColumnItem(clazz = classes[0], timeNow)
                Divider(modifier = Modifier.height(1.dp))
            }

            for (i in 1..classes.lastIndex) {
                val prevClass = classes[i - 1]
                val nextClass = classes[i]

                if (prevClass.startDate != nextClass.startDate) {
                    stickyHeader {
                        ScheduleColumnStickyHeader(nextClass.startDateTime.toLocalDate())
                    }
                }

                item(classes[i].id) {
                    ScheduleColumnItem(clazz = nextClass, timeNow)
                    Divider(modifier = Modifier.height(1.dp))
                }
            }
        }
    }
}

@Composable
private fun ScheduleColumnStickyHeader(date: LocalDate) {
    Surface(
        color = Color.LightGray,
        elevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun ScheduleColumnItemLayout(
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
private fun ScheduleColumnItem(clazz: Class, timeNow: ZonedDateTime) {
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

@Composable
private fun ClassTimeColumn(clazz: Class) {
    val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

    Column(modifier = Modifier.wrapContentSize()) {
        Text(
            clazz.startDateTime.format(formatter),
            maxLines = 1,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                clazz.endDateTime.format(formatter),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ClassDetailsColumn(clazz: Class) {
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
                    Text(clazz.details, modifier = Modifier.padding(bottom = 4.dp))
                }
            }
            if (clazz.location != null) {
                Text(
                    clazz.location,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ClassStatusColumn(
    status: ClassStatus,
    timeDifference: Long,
    timeNow: ZonedDateTime,
    clazz: Class
) {
    Column {
        val text = when (status) {
            ClassStatus.NOT_STARTED -> stringResource(
                R.string.class_starts_in,
                when {
                    timeDifference < 1 -> "<1min"
                    timeDifference < 60 -> "${timeDifference}min"
                    timeDifference > 1440 -> "${timeDifference / 1440}d"
                    else -> "${timeDifference / 60}h"
                }
            )

            ClassStatus.IN_PROGRESS -> {
                val timeDifference = timeNow.until(clazz.endDateTime, ChronoUnit.MINUTES)
                stringResource(
                    R.string.class_ends_in,
                    if (timeDifference < 1)
                        "<1min"
                    else
                        "${timeDifference}min"
                )
            }

            ClassStatus.ENDED -> stringResource(R.string.class_ended)
        }

        Text(
            text,
            textAlign = TextAlign.Right,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.width(IntrinsicSize.Min)
        )
    }
}

private enum class ClassStatus {
    NOT_STARTED,
    IN_PROGRESS,
    ENDED,
}

@Preview(showBackground = true)
@Composable
fun ScheduleColumnItemPreview() {
    UEKScheduleTheme {
        val c = Class(
            subject = "Administrowanie sieciami komputerowymi",
            teacher = "mgr Jakub Kanclerz",
            startDateTime = ZonedDateTime.now(),
            endDateTime = ZonedDateTime.now().plusMinutes(90L),
            location = "Paw. A 014 lab. Win 8.1, Office16",
            type = "ćwiczenia"
        )
        ScheduleColumnItem(clazz = c, ZonedDateTime.now())
    }
}
