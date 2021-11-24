package com.github.pwoicik.uekschedule.screen.editGroups

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.database.Group

@Composable
internal fun GroupColumn(
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
internal fun GroupColumnItem(group: Group, onDeleteGroup: (id: Long) -> Unit) {
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
