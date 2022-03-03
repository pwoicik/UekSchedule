package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.manageGroups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SimpleListScaffold
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.AddGroupsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ManageGroupsScreen(
    navigator: DestinationsNavigator,
    viewModel: ManageGroupsViewModel = hiltViewModel()
) {
    val savedGroups by viewModel.savedGroups.collectAsState()

    SimpleListScaffold(
        title = stringResource(R.string.your_groups),
        items = savedGroups,
        emptyListMessage = stringResource(R.string.no_saved_groups),
        onAddItemContentDescription = stringResource(R.string.add_group),
        onAddItem = {
            navigator.navigate(AddGroupsScreenDestination)
        }
    ) { group ->
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .padding(start = 8.dp)
        ) {
            Text(group.name)
            IconButton(
                onClick = {
                    viewModel.deleteGroup(group)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.delete_group)
                )
            }
        }
    }
}
