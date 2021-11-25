package com.github.pwoicik.uekschedule.screen.savedGroupsScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.components.LoadingSpinnerCentered
import com.github.pwoicik.uekschedule.components.NoSavedGroups
import com.github.pwoicik.uekschedule.database.ScheduleViewModel

@Composable
fun SavedGroupsScreen(
    viewModel: ScheduleViewModel,
    onAddGroups: () -> Unit,
    goBack: () -> Unit
) {
    val savedGroups by viewModel.groups.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.your_groups))
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "")
                    }
                },
                actions = {
                    IconButton(onClick = onAddGroups) {
                        Icon(Icons.Filled.Add, contentDescription = "")
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when {
                savedGroups == null ->
                    LoadingSpinnerCentered()
                savedGroups!!.isEmpty() ->
                    NoSavedGroups(onAddGroups)
                else ->
                    GroupsColumn(
                        groups = savedGroups!!,
                        onDeleteGroup = viewModel::deleteGroup
                    )
            }
        }
    }
}
