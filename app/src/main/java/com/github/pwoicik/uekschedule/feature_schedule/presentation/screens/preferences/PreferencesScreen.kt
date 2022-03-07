package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.preferences

import android.os.Build
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Preferences
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.preferences.components.PreferencesScaffold
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun PreferencesScreen(
    navigator: DestinationsNavigator,
    viewModel: PreferencesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    PreferencesScaffold(
        onNavigateBack = navigator::navigateUp
    ) {
        PreferencesItem(
            label = stringResource(R.string.dynamic_theme),
            description = stringResource(R.string.dynamic_theme_description) +
                    "\n" +
                    stringResource(R.string.requires_sdk_31)
        ) {
            Checkbox(
                checked = state.isDynamicTheme,
                enabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
                onCheckedChange = viewModel::setDynamicTheme
            )
        }
        PreferencesItem(label = stringResource(R.string.select_theme)) {
            var isDropdownExpanded by remember { mutableStateOf(false) }
            Surface(
                tonalElevation = 1.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { isDropdownExpanded = true }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .padding(vertical = 8.dp)
                        .animateContentSize()
                ) {
                    Text(
                        text = stringResource(state.theme.stringRes),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }

                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                    modifier = Modifier.selectableGroup()
                ) {
                    for (theme in Preferences.Theme.values(state.isDynamicTheme)) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(theme.stringRes),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            contentPadding = PaddingValues(horizontal = 24.dp),
                            onClick = {
                                viewModel.setTheme(theme)
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PreferencesItem(
    label: String,
    description: String? = null,
    action: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )
            description?.let { description ->
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .alpha(0.8f)
                )
            }
        }
        action()
    }
}
