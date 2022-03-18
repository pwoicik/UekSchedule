package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.main.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithError
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithLoading
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithSuccess
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.main.MainScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenScaffold(
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
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                when(it.visuals) {
                    is SnackbarVisualsWithPending -> MainScreenSnackbarWithLoading(it)
                    is SnackbarVisualsWithLoading -> MainScreenSnackbarWithLoading(it)
                    is SnackbarVisualsWithSuccess -> MainScreenDismissibleActionSnackbar(it)
                    is SnackbarVisualsWithError -> MainScreenDismissibleActionSnackbar(
                        snackbarData = it,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        },
        content = content
    )
}
