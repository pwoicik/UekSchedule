package com.github.pwoicik.uekschedule.feature_schedule.presentation.components.scheduleEntriesList

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleEntriesListScaffold(
    title: String,
    isSearchFieldVisible: Boolean,
    searchValue: TextFieldValue,
    onSearchValueChange: (TextFieldValue) -> Unit,
    isFabVisible: Boolean,
    onFabClick: () -> Unit,
    isRefreshing: Boolean,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onSearchValueClear: () -> Unit = { onSearchValueChange(TextFieldValue()) },
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: ScheduleEntriesListScaffoldColors = ScheduleEntriesListScaffoldColors.default(),
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        topBar = {
            SmallTopBarWithSearch(
                title = { Text(title) },
                isSearchFieldVisible = isSearchFieldVisible,
                searchValue = searchValue,
                onSearchValueChange = onSearchValueChange,
                onSearchValueClear = onSearchValueClear,
                navigationIcon = navigationIcon,
                actions = actions,
                colors = colors.topBarColors
            )
        },
        floatingActionButton = {
            if (isFabVisible) {
                FloatingActionButton(
                    containerColor = colors.fabContainerColor,
                    contentColor = colors.fabContentColor,
                    onClick = onFabClick
                ) {
                    Icon(
                        imageVector = Icons.Default.VerticalAlignCenter,
                        contentDescription = stringResource(R.string.scroll_to_today)
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
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
                    containerColor = colors.fabContainerColor,
                    contentColor = colors.fabContentColor,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        }
    }
}

data class ScheduleEntriesListScaffoldColors(
    val topBarColors: SmallTopBarWithSearchColors,
    val fabContainerColor: Color,
    val fabContentColor: Color
) {
    companion object {
        @Composable
        fun default(
            topBarColors: SmallTopBarWithSearchColors = SmallTopBarWithSearchColors.default(),
            fabContainerColor: Color = topBarColors.containerColor,
            fabContentColor: Color = topBarColors.titleColor
        ): ScheduleEntriesListScaffoldColors = ScheduleEntriesListScaffoldColors(
            topBarColors, fabContainerColor, fabContentColor
        )
    }
}
