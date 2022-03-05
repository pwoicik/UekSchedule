package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.schedule.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VerticalAlignCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarWithError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDestinationScaffold(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    fabPadding: PaddingValues,
    onFabClick: () -> Unit,
    onRefreshButtonClick: () -> Unit,
    onPreferencesButtonClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    snackbarPadding: PaddingValues,
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        topBar = {
            ScheduleDestinationTopBar(
                searchText = searchText,
                onSearchTextChange = onSearchTextChange,
                onRefreshButtonClick = onRefreshButtonClick,
                onPreferencesButtonClick = onPreferencesButtonClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick,
                modifier = Modifier
                    .padding(fabPadding)
            ) {
                Icon(
                    imageVector = Icons.Default.VerticalAlignCenter,
                    contentDescription = stringResource(R.string.scroll_to_today)
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                SnackbarWithError(snackbarData = snackbarData, padding = snackbarPadding)
            }
        }
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.padding(innerPadding),
            content = content
        )
    }
}
