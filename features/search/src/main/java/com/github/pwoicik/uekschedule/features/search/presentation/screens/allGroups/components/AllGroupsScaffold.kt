package com.github.pwoicik.uekschedule.features.search.presentation.screens.allGroups.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.common.R
import com.github.pwoicik.uekschedule.presentation.components.SearchTextField
import com.github.pwoicik.uekschedule.presentation.components.SnackbarHost
import com.github.pwoicik.uekschedule.presentation.components.searchTextFieldColors
import kotlinx.coroutines.job

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AllGroupsScaffold(
    searchFieldValue: TextFieldValue,
    onSearchValueChange: (TextFieldValue) -> Unit,
    focus: Boolean,
    snackbarHostState: SnackbarHostState,
    content: @Composable (PaddingValues) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(focus) {
        coroutineContext.job.invokeOnCompletion {
            if (focus) focusRequester.requestFocus()
        }
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
        content = content
    )
}
