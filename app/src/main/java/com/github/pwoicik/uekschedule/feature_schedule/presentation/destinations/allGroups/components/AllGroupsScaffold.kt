package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.allGroups.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.*
import com.google.accompanist.insets.statusBarsPadding

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun AllGroupsScaffold(
    searchFieldValue: TextFieldValue,
    onSearchValueChange: (TextFieldValue) -> Unit,
    snackbarHostState: SnackbarHostState,
    snackbarPadding: PaddingValues = PaddingValues(),
    content: @Composable (PaddingValues) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            Surface {
                SearchTextField(
                    value = searchFieldValue,
                    onValueChange = onSearchValueChange,
                    singleLine = true,
                    placeholder = stringResource(R.string.search_groups),
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
                        SnackbarWithError(snackbarData = snackbarData, padding = snackbarPadding)
                    is SnackbarVisualsWithSuccess ->
                        SnackbarWithSuccess(snackbarData = snackbarData, padding = snackbarPadding)
                }
            }
        },
        content = content
    )
}
