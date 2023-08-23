package com.github.pwoicik.uekschedule.presentation.navigation.screens.main.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.pwoicik.uekschedule.presentation.navigation.screens.main.MainScreenDestination
import com.github.pwoicik.uekschedule.presentation.util.zero
import com.ramcosta.composedestinations.spec.DestinationSpec

@Composable
internal fun MainScreenScaffold(
    currentDestination: DestinationSpec<*>?,
    onDestinationClick: (MainScreenDestination) -> Unit,
    snackbarHostState: SnackbarHostState,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.zero(),
        bottomBar = {
            Box(modifier = Modifier.imePadding())
            MainScreenBottomBar(
                currentDestination = currentDestination,
                onDestinationClick = onDestinationClick
            )
        },
        snackbarHost = { MainScreenSnackbarHost(hostState = snackbarHostState) },
        content = content
    )
}
