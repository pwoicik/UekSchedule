package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.addGroups.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.addGroups.AddGroupsEvent
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.addGroups.AddGroupsScreenState
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.addGroups.AddGroupsViewModel
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun AddGroupsSelectionList(
    viewModel: AddGroupsViewModel,
    state: AddGroupsScreenState
) {
    val savedGroups by viewModel.savedGroups.collectAsState()
    val filteredGroups by derivedStateOf { state.filteredGroups }

    Column {
        Surface(elevation = 24.dp) {
            AddGroupsTextFiled(
                value = state.searchText,
                onValueChange = { newText ->
                    viewModel.event(AddGroupsEvent.SearchTextChange(newText))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )
        }

        Surface {
            Column(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(8.dp)
            ) {
                AnimatedVisibility(state.selectedGroups.isNotEmpty()) {
                    AddGroupsSelectedGroupsRow(
                        selectedGroups = state.selectedGroups,
                        onUnselectGroup = {
                            viewModel.event(AddGroupsEvent.UnselectGroup(it))
                        }
                    )
                    Spacer(modifier = Modifier.height(42.dp))
                }

                AnimatedVisibility(
                    visible = filteredGroups.isNotEmpty(),
                    enter = slideInVertically(),
                    exit = slideOutVertically()
                ) {
                    LazyColumn(modifier = Modifier.selectableGroup()) {
                        items(filteredGroups) { group ->
                            val isSaved = group in savedGroups
                            val isSelected = group in state.selectedGroups

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = isSelected,
                                        enabled = !isSaved
                                    ) {
                                        viewModel.event(AddGroupsEvent.SelectGroup(group))
                                    }
                            ) {
                                if (isSaved)
                                    Text(
                                        text = stringResource(R.string.group_saved, group.name),
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .alpha(0.5f)
                                    )
                                else
                                    Text(group.name, modifier = Modifier.padding(8.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                AnimatedVisibility(
                    visible = filteredGroups.isEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(stringResource(R.string.no_group_found))
                    }
                }
            }
        }
    }
}
