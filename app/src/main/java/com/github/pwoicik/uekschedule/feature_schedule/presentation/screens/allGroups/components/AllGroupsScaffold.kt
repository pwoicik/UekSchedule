package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.allGroups.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.Constants
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SearchTextField
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarHost
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.searchTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllGroupsScaffold(
    searchFieldValue: TextFieldValue,
    onSearchValueChange: (TextFieldValue) -> Unit,
    focus: Boolean,
    snackbarHostState: SnackbarHostState,
    content: @Composable (PaddingValues) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(focus) {
        if (focus) focusRequester.requestFocus()
    }

    val bottomPadding = with(LocalDensity.current) {
        val insets = WindowInsets
        insets.ime.getBottom(this).toDp().coerceAtLeast(
            insets.navigationBars.getBottom(this).toDp() + Constants.BottomBarHeight
        )
    }
    Scaffold(
        topBar = {
            Surface {
                SearchTextField(
                    value = searchFieldValue,
                    onValueChange = onSearchValueChange,
                    singleLine = true,
                    placeholder = stringResource(R.string.search_groups),
                    colors = TextFieldDefaults.searchTextFieldColors(
                        cursorColor = MaterialTheme.colorScheme.secondary,
                        textColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.padding(bottom = bottomPadding),
        content = content
    )
}
