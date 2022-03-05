package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.schedule.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VerticalAlignCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.CircularProgressIndicator
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarWithError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDestinationScaffold(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    isFabVisible: Boolean,
    onFabClick: () -> Unit,
    fabPadding: PaddingValues,
    isRefreshing: Boolean,
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
            if (isFabVisible) {
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
            }        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                SnackbarWithError(snackbarData = snackbarData, padding = snackbarPadding)
            }
        }
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            content()
            AnimatedVisibility(
                visible = isRefreshing,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                CircularProgressIndicator(
                    isSpinning = isRefreshing,
                    color = MaterialTheme.colorScheme.onTertiary,
                    backgroundColor = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        }
    }
}
