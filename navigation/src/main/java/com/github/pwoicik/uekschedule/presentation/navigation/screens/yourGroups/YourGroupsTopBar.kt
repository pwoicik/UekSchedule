package com.github.pwoicik.uekschedule.presentation.navigation.screens.yourGroups

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.github.pwoicik.uekschedule.common.R
import com.github.pwoicik.uekschedule.features.groups.presentation.screens.groupSubjects.destinations.GroupSubjectsScreenDestination
import com.github.pwoicik.uekschedule.presentation.navigation.appDestination

@Composable
internal fun YourGroupsTopBar(
    currentNavBackStackEntry: NavBackStackEntry?,
    onChangeDestination: (YourGroupsDestination) -> Unit,
    onNavigateBack: () -> Unit
) {
    @Suppress("NAME_SHADOWING")
    Crossfade(
        targetState = currentNavBackStackEntry,
        modifier = Modifier.statusBarsPadding()
    ) { currentNavBackStackEntry ->
        val currentDestination = currentNavBackStackEntry?.appDestination()
        val currentYourGroupsDestination by remember {
            derivedStateOf {
                currentDestination?.let { currentDestination ->
                    YourGroupsDestination.values().find { destination ->
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
                        val spin by animateFloatAsState(if (isDropdownExpanded) -180f else 0f)
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
                            YourGroupsDestination.values().forEach { destination ->
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
