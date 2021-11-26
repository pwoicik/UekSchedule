package com.github.pwoicik.uekschedule.screen.savedGroupsScreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.database.Group

@Composable
fun GroupsColumn(
    groups: List<Group>,
    onDeleteGroup: (id: Long) -> Unit
) {
    LazyColumn {
        items(
            items = groups,
            key = Group::id
        ) { group ->
            GroupColumnItem(group, onDeleteGroup)
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
