package com.github.pwoicik.uekschedule.screen.scheduleScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.github.pwoicik.uekschedule.components.NoClasses
import com.github.pwoicik.uekschedule.database.Class
import com.github.pwoicik.uekschedule.database.ScheduleViewModel
import com.github.pwoicik.uekschedule.ui.theme.UEKScheduleTheme
import java.time.ZonedDateTime

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    onAddGroup: () -> Unit
) {
    val groups by viewModel.groups.collectAsState()
    val classes by viewModel.classes.collectAsState()

    when {
        groups.isNullOrEmpty() -> {
            NoSavedGroups(onAddGroup)
        }
        classes.isNullOrEmpty() -> {
            NoClasses()
        }
        else -> {
            ScheduleClassesColumn(classes!!, viewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleColumnItemPreview() {
    UEKScheduleTheme {
        val c = Class(
            subject = "Introduction to Computer Science",
            teacher = "dr John Doe",
            startDateTime = ZonedDateTime.now(),
            endDateTime = ZonedDateTime.now().plusMinutes(90L),
            location = "Paw. A 014 lab. Win 8.1, Office16",
            type = "workshop"
        )
        ScheduleColumnItem(clazz = c, ZonedDateTime.now())
    }
}
