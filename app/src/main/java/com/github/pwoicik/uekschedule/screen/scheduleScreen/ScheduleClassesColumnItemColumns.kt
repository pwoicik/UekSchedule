package com.github.pwoicik.uekschedule.screen.scheduleScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.database.Class
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

@Composable
fun ClassTimeColumn(clazz: Class) {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

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
fun ClassDetailsColumn(clazz: Class) {
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
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colors.error
                ) {
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
fun ClassStatusColumn(
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
                @Suppress("NAME_SHADOWING")
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
            color = if (status == ClassStatus.IN_PROGRESS)
                MaterialTheme.colors.secondary
            else
                MaterialTheme.colors.onSurface,
            modifier = Modifier.width(IntrinsicSize.Min)
        )
    }
}
