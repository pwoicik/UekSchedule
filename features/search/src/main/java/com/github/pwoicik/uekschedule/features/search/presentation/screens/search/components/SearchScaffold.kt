package com.github.pwoicik.uekschedule.features.search.presentation.screens.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.features.search.presentation.screens.search.DataState
import com.github.pwoicik.uekschedule.features.search.presentation.screens.search.SearchPages
import com.github.pwoicik.uekschedule.features.search.presentation.screens.search.SearchableSchedulablesState
import com.github.pwoicik.uekschedule.presentation.components.CircularProgressIndicator
import com.github.pwoicik.uekschedule.presentation.components.SearchTextField
import com.github.pwoicik.uekschedule.presentation.components.SnackbarHost
import com.github.pwoicik.uekschedule.presentation.components.searchTextFieldColors
import com.github.pwoicik.uekschedule.presentation.util.zero
import com.github.pwoicik.uekschedule.resources.R
import kotlinx.coroutines.job

@Composable
@NonRestartableComposable
internal fun SearchScaffold(
    searchableSchedulablesState: SearchableSchedulablesState,
    onSearchValueChange: (TextFieldValue) -> Unit,
    currentPage: SearchPages,
    onPageChange: (SearchPages) -> Unit,
    focus: Boolean,
    snackbarHostState: SnackbarHostState,
    content: @Composable () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.zero(),
        topBar = {
            SearchTopBar(
                searchValue = searchableSchedulablesState.searchValue,
                onSearchValueChange = onSearchValueChange,
                currentPage = currentPage,
                onPageChange = onPageChange,
                focus = focus
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        SearchCrossfade(
            searchableSchedulablesState = searchableSchedulablesState,
            paddingValues = innerPadding,
            content = content
        )
    }
}

@Composable
private fun SearchTopBar(
    searchValue: TextFieldValue,
    onSearchValueChange: (TextFieldValue) -> Unit,
    currentPage: SearchPages,
    onPageChange: (SearchPages) -> Unit,
    focus: Boolean,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(focus) {
        coroutineContext.job.invokeOnCompletion {
            if (focus) focusRequester.requestFocus()
        }
    }

    Column {
        SearchTextField(
            value = searchValue,
            onValueChange = onSearchValueChange,
            singleLine = true,
            placeholder = stringResource(R.string.search_groups),
            colors = TextFieldDefaults.searchTextFieldColors(
                cursorColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        TabRow(
            selectedTabIndex = currentPage.ordinal,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[currentPage.ordinal]),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        ) {
            SearchPages.entries.forEach { page ->
                Tab(
                    selected = currentPage == page,
                    text = { Text(stringResource(id = page.title)) },
                    selectedContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unselectedContentColor = MaterialTheme.colorScheme.secondary,
                    onClick = {
                        onPageChange(page)
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchCrossfade(
    searchableSchedulablesState: SearchableSchedulablesState,
    paddingValues: PaddingValues,
    content: @Composable () -> Unit
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Crossfade(
            targetState = searchableSchedulablesState.dataState,
            label = "data state crossfade"
        ) { state ->
            when (state) {
                DataState.NO_CONNECTION -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudOff,
                            contentDescription = stringResource(R.string.couldnt_connect),
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }

                DataState.SUCCESS -> {
                    content()
                }

                else -> Unit
            }
        }
        AnimatedVisibility(
            visible = searchableSchedulablesState.isLoading,
            enter = slideInVertically { -it },
            exit = slideOutVertically { -it }
        ) {
            CircularProgressIndicator(
                isSpinning = searchableSchedulablesState.isLoading,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}
