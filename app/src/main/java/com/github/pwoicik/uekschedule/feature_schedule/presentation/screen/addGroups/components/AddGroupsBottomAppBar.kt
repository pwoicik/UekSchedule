package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addGroups.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.pwoicik.uekschedule.R

@Composable
fun AddGroupsBottomAppBar(
    onNavigateBack: () -> Unit,
    selectedGroupsActionButtonsEnabled: Boolean,
    onClearSelectedGroups: () -> Unit,
    onSaveSelectedGroups: () -> Unit
) {
    BottomAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
    ) {
        IconButton(
            onClick = onNavigateBack
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = stringResource(R.string.navigate_back)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            enabled = selectedGroupsActionButtonsEnabled,
            onClick = onClearSelectedGroups,
        ) {
            Icon(
                imageVector = Icons.Default.ClearAll,
                contentDescription = stringResource(R.string.clear_selected_groups)
            )
        }
        IconButton(
            enabled = selectedGroupsActionButtonsEnabled,
            onClick = onSaveSelectedGroups
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = stringResource(R.string.save_selected_groups)
            )
        }
    }
}
