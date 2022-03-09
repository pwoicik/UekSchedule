package com.github.pwoicik.uekschedule.feature_schedule.presentation.components.scheduleEntriesList

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleEntriesList(
    scheduleEntries: Map<LocalDate, List<ScheduleEntry>>,
    timeNow: LocalDateTime,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        for ((date, entries) in scheduleEntries) {
            scheduleEntriesListStickyHeader(date)
            items(items = entries) { entry ->
                ScheduleEntriesListItem(scheduleEntry = entry, status = entry.status(timeNow))
                Divider(modifier = Modifier.alpha(0.5f))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.scheduleEntriesListStickyHeader(
    startDate: LocalDate
) {
    stickyHeader {
        Surface(
            tonalElevation = 4.dp,
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
fun ScheduleEntriesListItem(
    scheduleEntry: ScheduleEntry,
    status: ScheduleEntryStatus
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .alpha(
                if (status is ScheduleEntryStatus.Ended) 0.4f
                else 1f
            )
            .padding(10.dp)
    ) {
        ScheduleEntryTimesColumn(
            status = status,
            startDateTime = scheduleEntry.startDateTime,
            endDateTime = scheduleEntry.endDateTime
        )
        ScheduleEntrySummaryColumn(
            name = scheduleEntry.name,
            teachers = scheduleEntry.teachers,
            details = scheduleEntry.details,
            type = scheduleEntry.type,
            location = scheduleEntry.location
        )
        ScheduleEntryStatusColumn(status)
    }
}

@Composable
private fun ScheduleEntryTimesColumn(
    status: ScheduleEntryStatus,
    startDateTime: LocalDateTime,
    endDateTime: LocalDateTime
) {
    Column {
        val startAlpha = if (status is ScheduleEntryStatus.InProgress) 0.6f else 1f
        val endAlpha = if (status is ScheduleEntryStatus.NotStarted) 0.6f else 1f
        Text(
            text = startDateTime.format(timeFormatter),
            modifier = Modifier.alpha(startAlpha)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = endDateTime.format(timeFormatter),
            modifier = Modifier.alpha(endAlpha)
        )
    }
}

@Composable
private fun RowScope.ScheduleEntrySummaryColumn(
    name: String,
    teachers: List<String>,
    details: String?,
    type: String?,
    location: String?
) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .weight(0.6f, fill = true)
            .padding(horizontal = 16.dp)
    ) {
        Text(name)

        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodyMedium,
            LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            if (teachers.isNotEmpty()) {
                Box {
                    teachers.forEach { Text(it) }
                }
            }

            if (details == null) {
                if (type != null) {
                    Text(type)
                }
            } else {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.tertiary
                ) {
                    Column {
                        type?.let { Text(it) }
                        Text(details)
                    }
                }
            }

            if (location != null) {
                val match = htmlAnchorRegex.find(location)
                if (match != null) {
                    val url = match.groupValues[1]
                    val text = match.groupValues[2].trim()

                    val intent = remember {
                        Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clickable {
                                context.startActivity(intent)
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = stringResource(R.string.open_in_browser),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = text,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.align(Alignment.Bottom)
                        )
                    }
                } else {
                    Text(
                        text = location,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduleEntryStatusColumn(
    status: ScheduleEntryStatus
) {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.End) {
        ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
            when (status) {
                is ScheduleEntryStatus.NotStarted -> {
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
                is ScheduleEntryStatus.InProgress -> {
                    val endsIn = status.minutesRemaining
                    val text = when {
                        endsIn < 1 -> stringResource(R.string.class_ends_in_less_than_1_min)
                        else -> stringResource(R.string.class_ends_in_minutes, endsIn)
                    }

                    Text(
                        text = text,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.End
                    )
                }
                is ScheduleEntryStatus.Ended -> {
                    Text(
                        text = stringResource(R.string.class_ended),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("eee dd MMM yyyy")
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

private val htmlAnchorRegex = """<a href="(.+)">(.+)</a>""".toRegex()
