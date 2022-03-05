package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.main.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.main.MainScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenScaffold(
    currentDestination: DestinationSpec<*>?,
    onDestinationClick: (MainScreenDestination) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            MainScreenBottomBar(
                currentDestination = currentDestination,
                onDestinationClick = onDestinationClick
            )
        },
        content = content
    )
}
