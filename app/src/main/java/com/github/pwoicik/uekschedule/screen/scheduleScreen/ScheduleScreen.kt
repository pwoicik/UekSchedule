package com.github.pwoicik.uekschedule.screen.scheduleScreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.components.LoadingSpinnerCentered
import com.github.pwoicik.uekschedule.components.NoSavedGroups
import com.github.pwoicik.uekschedule.database.Class
import com.github.pwoicik.uekschedule.database.ScheduleViewModel
import com.github.pwoicik.uekschedule.ui.theme.UEKScheduleTheme
import kotlinx.coroutines.launch
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

    val scaffoldState = rememberScaffoldState()

    val connectionErrorMessage = stringResource(R.string.couldnt_connect)
    val coroutineScope = rememberCoroutineScope()
    val refresh = {
        viewModel.refresh(
            onError = {
                if (scaffoldState.snackbarHostState.currentSnackbarData == null)
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = connectionErrorMessage,
                            duration = SnackbarDuration.Indefinite
                        )
                    }
            },
            onSuccess = {
                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
            }
        )
    }

    LaunchedEffect(groups) {
        refresh()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
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
        },
        snackbarHost = {
            SnackbarHost(it) { snackbarData ->
                Snackbar(
                    shape = RoundedCornerShape(8.dp),
                    action = {
                        IconButton(onClick = refresh) {
                            Icon(Icons.Filled.Refresh, contentDescription = "")
                        }
                    },
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(snackbarData.message)
                }
            }
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
                    ScheduleClassesColumn(
                        classes = classes!!,
                        viewModel = viewModel,
                        onRefresh = refresh
                    )
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
