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
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxSize()
            .alpha(
                if (status is ScheduleEntryStatus.Ended) 0.4f
                else 1f
            )
            .padding(10.dp)
    ) {
        Column {
            val startAlpha = if (status is ScheduleEntryStatus.InProgress) 0.6f else 1f
            val endAlpha = if (status is ScheduleEntryStatus.NotStarted) 0.6f else 1f
            Text(
                text = scheduleEntry.startDateTime.format(timeFormatter),
                modifier = Modifier.alpha(startAlpha)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = scheduleEntry.endDateTime.format(timeFormatter),
                modifier = Modifier.alpha(endAlpha)
            )
        }

        Column(
            modifier = Modifier
                .weight(0.6f, fill = true)
                .padding(horizontal = 16.dp)
        ) {
            Text(scheduleEntry.name)
            Spacer(modifier = Modifier.height(4.dp))

            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyMedium,
                LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                scheduleEntry.teachers?.let { teachers ->
                    if (teachers.isNotEmpty()) {
                        scheduleEntry.teachers.forEach { teacher ->
                            Text(teacher)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

                if (scheduleEntry.details == null) {
                    if (scheduleEntry.type != null) Text(scheduleEntry.type)
                } else {
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colorScheme.tertiary
                    ) {
                        Text(scheduleEntry.type!!)
                        Text(scheduleEntry.details)
                    }
                }

                if (scheduleEntry.location != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    val match = htmlAnchorRegex.find(scheduleEntry.location)
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
                            text = scheduleEntry.location,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

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
}

private val dateFormatter = DateTimeFormatter.ofPattern("eee dd MMM yyyy")
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

private val htmlAnchorRegex = """<a href="(.+)">(.+)</a>""".toRegex()
