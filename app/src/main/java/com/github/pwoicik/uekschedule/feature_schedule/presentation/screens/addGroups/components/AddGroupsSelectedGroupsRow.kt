package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.addGroups.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group

@Composable
fun AddGroupsSelectedGroupsRow(
    selectedGroups: List<Group>,
    onUnselectGroup: (Group) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(modifier = modifier) {
        for (i in selectedGroups.lastIndex downTo 0) {
            val group = selectedGroups[i]
            item {
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            onUnselectGroup(group)
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 6.dp, horizontal = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(
                                R.string.unselect_group
                            ),
                            modifier = Modifier
                                .size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = group.name,
                            style = MaterialTheme.typography.body2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}
