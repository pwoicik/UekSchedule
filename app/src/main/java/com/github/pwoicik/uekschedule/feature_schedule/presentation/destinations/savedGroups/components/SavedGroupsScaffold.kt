package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.savedGroups.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.pwoicik.uekschedule.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedGroupsScaffold(
    currentScreen: Int,
    onScreenChange: (Int) -> Unit,
    onAddItem: () -> Unit,
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues,
    content: @Composable (Int) -> Unit
) {
    Scaffold(
        topBar = {
            SavedGroupsTopBar(
                currentScreen = currentScreen,
                onScreenChange = onScreenChange,
                onAddItem = onAddItem,
                onAddItemContentDescription = stringResource(R.string.add_group)
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    modifier = Modifier.padding(contentPadding)
                )
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentScreen,
            modifier = Modifier
                .padding(innerPadding)
                .padding(contentPadding),
            content = content
        )
    }
}
