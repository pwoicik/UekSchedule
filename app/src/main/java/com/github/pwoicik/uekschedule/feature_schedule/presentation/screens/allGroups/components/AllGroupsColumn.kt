package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.allGroups.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group

@Composable
fun AllGroupsColumn(
    groups: List<Group>,
    onGroupClick: (Group) -> Unit,
    areGroupAddButtonsEnabled: Boolean,
    onGroupAddButtonClick: (Group) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 20.dp),
        modifier = modifier
    ) {
        items(groups) { group ->
            Surface(
                modifier = Modifier
                    .clickable(
                        enabled = areGroupAddButtonsEnabled,
                        onClickLabel = stringResource(R.string.preview_group)
                    ) {
                        onGroupClick(group)
                    }
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = group.name,
                        modifier = Modifier.weight(1f, fill = true)
                    )

                    IconButton(onClick = { onGroupAddButtonClick(group) }) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.save_group),
                                modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp)
                            )
                        }
                    }

                    Box(modifier = Modifier.padding(start = 4.dp)) {
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = stringResource(R.string.preview_group),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            Divider()
        }
    }
}
