package com.github.pwoicik.uekschedule.screen.editGroupsScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.api.model.Group

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun AddGroupPopup(
    availableGroups: List<Group>?,
    onDismiss: () -> Unit,
    onConfirmAddGroups: (List<Group>) -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var selectedGroups by remember { mutableStateOf(listOf<Group>()) }

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
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
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

                val keyboardController = LocalSoftwareKeyboardController.current
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(stringResource(R.string.add_group_textfield_placeholder))
                    },
                    enabled = availableGroups != null,
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            onConfirmAddGroups(selectedGroups)
                            onDismiss()
                        }
                    )
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 24.dp)
                )
                AvailableGroups(
                    availableGroups = availableGroups,
                    searchText = inputText,
                    selectedGroups = selectedGroups,
                    onSelectGroup = { selectedGroups = selectedGroups + it },
                    onUnselectGroup = { selectedGroups = selectedGroups - it }
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 12.dp)
                )

                Button(
                    enabled = selectedGroups.isNotEmpty(),
                    onClick = {
                        onConfirmAddGroups(selectedGroups)
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
fun ColumnScope.AvailableGroups(
    availableGroups: List<Group>?,
    searchText: String,
    selectedGroups: List<Group>,
    onSelectGroup: (Group) -> Unit,
    onUnselectGroup: (Group) -> Unit
) {
    if (availableGroups.isNullOrEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val filteredGroups = availableGroups.filter {
                val isNotSelected = it !in selectedGroups
                val matchesSearchText = it.name.lowercase().contains(searchText.lowercase())

                isNotSelected && matchesSearchText
            }

            if (selectedGroups.isNotEmpty()) {
                SelectedGroupsRow(
                    selectedGroups = selectedGroups,
                    onUnselectGroup = onUnselectGroup
                )
            }
            if (filteredGroups.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    Text(stringResource(R.string.no_group_found))
                }
            } else {
                AvailableGroupsList(
                    availableGroups = filteredGroups,
                    selectedGroups = selectedGroups,
                    onSelectGroup = onSelectGroup,
                )
            }

        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectedGroupsRow(
    selectedGroups: List<Group>,
    onUnselectGroup: (Group) -> Unit
) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        for (i in selectedGroups.lastIndex downTo 0) {
            val group = selectedGroups[i]
            item {
                Card(
                    border = BorderStroke(1.dp, MaterialTheme.colors.onSurface),
                    onClick = { onUnselectGroup(group) },
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .alpha(0.5f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 2.dp, horizontal = 6.dp)
                            .height(IntrinsicSize.Min)
                    ) {
                        ProvideTextStyle(MaterialTheme.typography.body2) {
                            Text(group.name)
                            Icon(Icons.Filled.Clear, contentDescription = "")
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun AvailableGroupsList(
    availableGroups: List<Group>,
    selectedGroups: List<Group>,
    onSelectGroup: (Group) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(
            items = availableGroups,
            key = Group::id
        ) { group ->
            val isSelected = group in selectedGroups

            Surface(
                color = if (isSelected) Color.LightGray else MaterialTheme.colors.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            onSelectGroup(group)
                        }
                    )
            ) {
                Text(
                    text = group.name,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
