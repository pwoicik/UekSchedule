package com.github.pwoicik.uekschedule.screen.editGroups

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.database.Group
import com.github.pwoicik.uekschedule.database.ScheduleViewModel
import com.github.pwoicik.uekschedule.api.model.Group as AvailableGroup

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EditGroupsScreen(viewModel: ScheduleViewModel, showPopup: Boolean) {
    @Suppress("NAME_SHADOWING")
    var showPopup by remember { mutableStateOf(showPopup) }

    val savedGroups by viewModel.groups.collectAsState()
    val availableGroups by viewModel.getAllAvailableGroups()

    val showPopupFun = { showPopup = true }
    val hidePopupFun = { showPopup = false }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (!savedGroups.isNullOrEmpty()) {
            GroupColumn(
                groups = savedGroups!!,
                onAddGroup = showPopupFun,
                onDeleteGroup = viewModel::deleteGroup
            )
        } else {
            NoSavedGroups(showPopupFun)
        }

        AnimatedVisibility(
            visible = showPopup,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            AddGroupPopup(
                availableGroups = availableGroups,
                onDismiss = hidePopupFun,
                onConfirmAddGroup = viewModel::addSchedule
            )
        }
    }
}

@Composable
fun AddGroupPopup(
    availableGroups: List<AvailableGroup>?,
    onDismiss: () -> Unit,
    onConfirmAddGroup: (id: Long) -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var selectedGroup: Long? by remember { mutableStateOf(null) }

    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true, dismissOnClickOutside = false)
    ) {
        Surface(
            elevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.65f)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .animateContentSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.add_group),
                        fontWeight = FontWeight.Bold,
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "")
                    }
                }

                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(stringResource(R.string.add_group_textfield_placeholder))
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )
                if (!availableGroups.isNullOrEmpty()) {
                    val filteredGroups = availableGroups.filter {
                        it.name.lowercase().contains(inputText.lowercase())
                    }
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(
                            items = filteredGroups,
                            key = AvailableGroup::id
                        ) { group ->
                            val isSelected = group.id.toLong() == selectedGroup

                            Surface(
                                color = if (isSelected) Color.LightGray else MaterialTheme.colors.surface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = isSelected,
                                        onClick = {
                                            selectedGroup = group.id.toLong()
                                        }
                                    )
                            ) {
                                Text(
                                    text = group.name,
                                    style = MaterialTheme.typography.body2,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        CircularProgressIndicator()
                    }
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                )

                Button(
                    enabled = selectedGroup != null,
                    onClick = {
                        onConfirmAddGroup(selectedGroup!!)
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(R.string.add))
                }
            }
        }
    }
}

@Composable
fun GroupColumn(
    groups: List<Group>,
    onAddGroup: () -> Unit,
    onDeleteGroup: (id: Long) -> Unit
) {
    Column {
        Surface(elevation = 6.dp) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp, horizontal = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.your_groups),
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onAddGroup) {
                    Icon(Icons.Filled.Add, contentDescription = "")
                }
            }
        }

        LazyColumn {
            items(
                items = groups,
                key = Group::id
            ) { group ->
                GroupColumnItem(group, onDeleteGroup)
            }
        }
    }
}

@Composable
fun GroupColumnItem(group: Group, onDeleteGroup: (id: Long) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Text(
            text = group.name,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = { onDeleteGroup(group.id) }
        ) {
            Icon(Icons.Outlined.Delete, contentDescription = "")
        }
    }
}

@Composable
fun NoSavedGroups(onAddGroup: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.no_saved_groups))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAddGroup) {
            Text(stringResource(R.string.add_group))
        }
    }
}
