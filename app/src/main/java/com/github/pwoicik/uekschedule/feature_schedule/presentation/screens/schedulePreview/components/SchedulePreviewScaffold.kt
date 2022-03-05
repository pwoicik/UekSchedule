package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedulePreview.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.CircularProgressIndicator
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarWithError
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.statusBarsPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulePreviewScaffold(
    title: String,
    onNavigateBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    isRefreshing: Boolean,
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                SmallTopAppBar(
                    title = { Text(title) },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.Transparent,
                        navigationIconContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = stringResource(R.string.go_back)
                            )
                        }
                    },
                    modifier = Modifier.statusBarsPadding()
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                val bottomPadding = with (LocalDensity.current) {
                    LocalWindowInsets.current.navigationBars.bottom.toDp()
                }
                SnackbarWithError(
                    snackbarData = snackbarData,
                    padding = PaddingValues(bottom = bottomPadding)
                )
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
                    color = MaterialTheme.colorScheme.onSecondary,
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
