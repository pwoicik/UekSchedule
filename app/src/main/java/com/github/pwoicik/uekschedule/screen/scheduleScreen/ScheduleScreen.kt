package com.github.pwoicik.uekschedule.screen.scheduleScreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.components.LoadingSpinnerCentered
import com.github.pwoicik.uekschedule.components.NoSavedGroups
import com.github.pwoicik.uekschedule.database.Class
import com.github.pwoicik.uekschedule.database.ScheduleViewModel
import com.github.pwoicik.uekschedule.ui.theme.UEKScheduleTheme
import java.time.ZonedDateTime

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    toggleDarkMode: () -> Unit,
    onEditGroups: () -> Unit,
    onAddGroups: () -> Unit
) {
    val groups by viewModel.groups.collectAsState()
    val classes by viewModel.classes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(onClick = toggleDarkMode) {
                        Icon(
                            if (isSystemInDarkTheme())
                                Icons.Filled.LightMode
                            else
                                Icons.Filled.DarkMode,
                            contentDescription = ""
                        )
                    }

                    IconButton(onClick = onEditGroups) {
                        Icon(Icons.Filled.Edit, contentDescription = "")
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when {
                groups == null || classes == null ->
                    LoadingSpinnerCentered()
                groups!!.isEmpty() ->
                    NoSavedGroups(onAddGroups)
                classes!!.isEmpty() ->
                    NoClasses()
                else -> {
                    ScheduleClassesColumn(classes!!, viewModel)
                }
            }
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
