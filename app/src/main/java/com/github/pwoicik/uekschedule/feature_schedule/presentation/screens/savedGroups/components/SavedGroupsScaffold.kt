package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.savedGroups.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.Constants
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.savedGroups.SavedGroupsSection
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedGroupsScaffold(
    currentScreen: SavedGroupsSection,
    onScreenChange: (SavedGroupsSection) -> Unit,
    onAddItem: () -> Unit,
    snackbarHostState: SnackbarHostState,
    content: @Composable (SavedGroupsSection) -> Unit
) {
    Scaffold(
        topBar = {
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
                        Text(stringResource(currentScreen.title))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .rotate(spin)
                        )
                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false }
                        ) {
                            SavedGroupsSection.values().forEach { screen ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(screen.title),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    },
                                    onClick = {
                                        onScreenChange(screen)
                                        isDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.statusBarsPadding()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddItem,
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(
                        when (currentScreen) {
                            SavedGroupsSection.SavedGroups -> R.string.add_group
                            SavedGroupsSection.OtherActivities -> R.string.create_new_activity
                        }
                    )
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(snackbarData = snackbarData)
            }
        },
        modifier = Modifier
            .navigationBarsPadding()
            .padding(bottom = Constants.BottomBarHeight)
    ) { innerPadding ->
        Crossfade(
            targetState = currentScreen,
            modifier = Modifier.padding(innerPadding),
            content = content
        )
    }
}
