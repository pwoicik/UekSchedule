package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.allGroups.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.*
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.statusBarsPadding

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
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

    val insets = LocalWindowInsets.current
    val bottomPadding = with(LocalDensity.current) {
        insets.ime.bottom.toDp().coerceAtLeast(
            insets.navigationBars.bottom.toDp() + Constants.BottomBarHeight
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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                when (snackbarData.visuals) {
                    is SnackbarVisualsWithError ->
                        SnackbarWithError(snackbarData = snackbarData)
                    is SnackbarVisualsWithSuccess ->
                        SnackbarWithSuccess(snackbarData = snackbarData)
                }
            }
        },
        modifier = Modifier.padding(bottom = bottomPadding),
        content = content
    )
}
