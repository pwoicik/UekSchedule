package com.github.pwoicik.uekschedule.presentation.navigation.screens.yourGroups

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.github.pwoicik.uekschedule.features.groups.presentation.screens.groupSubjects.destinations.GroupSubjectsScreenDestination
import com.github.pwoicik.uekschedule.presentation.navigation.appDestination
import com.github.pwoicik.uekschedule.resources.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun YourGroupsTopBar(
    currentNavBackStackEntry: NavBackStackEntry?,
    onChangeDestination: (YourGroupsDestination) -> Unit,
    onNavigateBack: () -> Unit
) {
    @Suppress("NAME_SHADOWING")
    Crossfade(
        targetState = currentNavBackStackEntry,
        label = "current screen crossfade",
        modifier = Modifier.statusBarsPadding()
    ) { currentNavBackStackEntry ->
        val currentDestination = currentNavBackStackEntry?.appDestination()
        val currentYourGroupsDestination by remember {
            derivedStateOf {
                currentDestination?.let { currentDestination ->
                    YourGroupsDestination.entries.find { destination ->
                        destination.direction.route == currentDestination.route
                    }
                }
            }
        }
        when (currentDestination) {
            is GroupSubjectsScreenDestination -> {
                CenterAlignedTopAppBar(
                    title = {
                        Text(currentDestination.argsFrom(currentNavBackStackEntry).groupName)
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = stringResource(R.string.go_back)
                            )
                        }
                    }
                )
            }

            else -> {
                CenterAlignedTopAppBar(
                    title = {
                        var isDropdownExpanded by remember { mutableStateOf(false) }
                        val spin by animateFloatAsState(
                            targetValue = if (isDropdownExpanded) -180f else 0f,
                            label = "expand icon spin"
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { isDropdownExpanded = true }
                                .padding(start = 10.dp)
                        ) {
                            Text(
                                currentYourGroupsDestination?.let { stringResource(it.label) } ?: ""
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .graphicsLayer {
                                        rotationX = spin
                                        cameraDistance = 48 * density
                                    }
                            )
                        }
                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false }
                        ) {
                            YourGroupsDestination.entries.forEach { destination ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(destination.label),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    },
                                    onClick = {
                                        onChangeDestination(destination)
                                        isDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}
