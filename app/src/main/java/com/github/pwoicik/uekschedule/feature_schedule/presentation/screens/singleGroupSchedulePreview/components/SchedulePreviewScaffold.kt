package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.singleGroupSchedulePreview.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SmallTopBarWithSearchColors
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.scheduleEntriesList.ScheduleEntriesListScaffold
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.scheduleEntriesList.ScheduleEntriesListScaffoldColors

@Composable
fun SchedulePreviewScaffold(
    title: String,
    searchValue: TextFieldValue,
    onSearchValueChange: (TextFieldValue) -> Unit,
    onNavigateBack: () -> Unit,
    isFabVisible: Boolean,
    onFabClick: () -> Unit,
    isRefreshing: Boolean,
    snackbarHostState: SnackbarHostState,
    content: @Composable BoxScope.() -> Unit
) {
    var isSearchFieldVisible by rememberSaveable { mutableStateOf(false) }

    ScheduleEntriesListScaffold(
        title = title,
        isSearchFieldVisible = isSearchFieldVisible,
        searchValue = searchValue,
        onSearchValueChange = onSearchValueChange,
        onSearchValueClear = {
            onSearchValueChange(TextFieldValue())
            isSearchFieldVisible = false
        },
        isFabVisible = isFabVisible,
        onFabClick = onFabClick,
        isRefreshing = isRefreshing,
        snackbarHostState = snackbarHostState,
        colors = ScheduleEntriesListScaffoldColors.default(
            topBarColors = SmallTopBarWithSearchColors.default(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                leadingIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                titleColor = MaterialTheme.colorScheme.onTertiaryContainer,
                trailingIconsColor = MaterialTheme.colorScheme.onTertiaryContainer,
                indicatorsColor = MaterialTheme.colorScheme.tertiary
            ),
            fabContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            fabContentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = stringResource(R.string.go_back)
                )
            }
        },
        actions = {
            IconButton(onClick = { isSearchFieldVisible = true }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_entry)
                )
            }
        },
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding(),
        content = content
    )
}
