package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.classes.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Class
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.classes.ClassStatus
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.classes.status
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ClassesList(
    isUpdating: Boolean,
    classes: List<Class>,
    timeNow: ZonedDateTime,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isUpdating),
        onRefresh = onRefresh
    ) {
        LazyColumn {
            classesListStickyHeader(classes[0].startDate)
            item {
                ClassesListItem(
                    clazz = classes[0],
                    status = classes[0].status(timeNow)
                )
            }

            for (i in 1..classes.lastIndex) {
                val prevClazz = classes[i - 1]
                val clazz = classes[i]

                val isNextDay = clazz.startDate != prevClazz.startDate
                if (isNextDay) {
                    classesListStickyHeader(clazz.startDate)
                }
                item {
                    if (!isNextDay) Divider()
                    ClassesListItem(
                        clazz = clazz,
                        status = clazz.status(timeNow)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.classesListStickyHeader(
    startDate: LocalDate
) {
    stickyHeader {
        Surface(
            elevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = startDate.format(dateFormatter),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp)
            )
        }
    }
}

@Composable
fun ClassesListItem(
    clazz: Class,
    status: ClassStatus
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxSize()
            .alpha(
                if (status is ClassStatus.Ended) 0.4f
                else 1f
            )
            .padding(10.dp)
    ) {
        Column {
            val startAlpha = if (status is ClassStatus.InProgress) 0.6f else 1f
            val endAlpha = if (status is ClassStatus.NotStarted) 0.6f else 1f
            Text(
                text = clazz.startDateTime.format(timeFormatter),
                modifier = Modifier.alpha(startAlpha)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = clazz.endDateTime.format(timeFormatter),
                modifier = Modifier.alpha(endAlpha)
            )
        }

        Column(
            modifier = Modifier
                .weight(0.6f, fill = true)
                .padding(horizontal = 16.dp)
        ) {
            Text(clazz.subject)
            Spacer(modifier = Modifier.height(4.dp))

            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.body2,
                LocalContentAlpha provides ContentAlpha.medium
            ) {
                clazz.teachers.split(", ").forEach { teacher ->
                    Text(teacher)
                }
                Spacer(modifier = Modifier.height(4.dp))

                if (clazz.details == null) {
                    Text(clazz.type)
                } else {
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colors.error
                    ) {
                        Text(clazz.type)
                        Text(clazz.details)
                    }
                }

                if (clazz.location != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = clazz.location,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            ProvideTextStyle(MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Medium)) {
                when (status) {
                    is ClassStatus.NotStarted -> {
                        val startsIn = status.minutesToStart
                        val text = when {
                            startsIn < 1 ->
                                stringResource(R.string.class_starts_in_less_than_1_min)
                            startsIn < 60 ->
                                stringResource(R.string.class_starts_in_minutes, startsIn)
                            startsIn > 1440 -> {
                                val days = startsIn / 1440
                                context
                                    .resources
                                    .getQuantityString(
                                        R.plurals.class_starts_in_days,
                                        days.toInt(),
                                        days
                                    )
                            }
                            else ->
                                stringResource(
                                    R.string.class_starts_in_hours,
                                    startsIn / 60
                                )
                        }

                        Text(
                            text = text,
                            textAlign = TextAlign.End
                        )
                    }
                    is ClassStatus.InProgress -> {
                        val endsIn = status.minutesRemaining
                        val text = when {
                            endsIn < 1 -> stringResource(R.string.class_ends_in_less_than_1_min)
                            else -> stringResource(R.string.class_ends_in_minutes, endsIn)
                        }

                        Text(
                            text = text,
                            color = MaterialTheme.colors.secondary,
                            textAlign = TextAlign.End
                        )
                    }
                    is ClassStatus.Ended -> {
                        Text(
                            text = stringResource(R.string.class_ended),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("eee dd MMM yyyy")
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
