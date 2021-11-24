package com.github.pwoicik.uekschedule.screen.editGroupsScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.github.pwoicik.uekschedule.api.model.Group

@Composable
internal fun AddGroupPopup(
    availableGroups: List<Group>?,
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
                            key = Group::id
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
