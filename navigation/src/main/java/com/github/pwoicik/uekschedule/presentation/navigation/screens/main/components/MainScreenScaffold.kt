package com.github.pwoicik.uekschedule.presentation.navigation.screens.main.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.github.pwoicik.uekschedule.presentation.navigation.screens.main.MainScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreenScaffold(
    currentDestination: DestinationSpec<*>?,
    onDestinationClick: (MainScreenDestination) -> Unit,
    snackbarHostState: SnackbarHostState,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            MainScreenBottomBar(
                currentDestination = currentDestination,
                onDestinationClick = onDestinationClick
            )
        },
        snackbarHost = { MainScreenSnackbarHost(hostState = snackbarHostState) },
        content = content
    )
}
