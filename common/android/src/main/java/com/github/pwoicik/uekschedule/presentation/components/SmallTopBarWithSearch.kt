package com.github.pwoicik.uekschedule.presentation.components

import androidx.activity.addCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.resources.R
import kotlinx.coroutines.job

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopBarWithSearch(
    title: @Composable () -> Unit,
    isSearchFieldVisible: Boolean,
    dismissSearchField: () -> Unit,
    searchValue: TextFieldValue,
    onSearchValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    onSearchValueClear: () -> Unit = { onSearchValueChange(TextFieldValue()) },
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: SmallTopBarWithSearchColors = SmallTopBarWithSearchColors.default()
) {
    val backPressDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    LaunchedEffect(isSearchFieldVisible) {
        if (!isSearchFieldVisible) return@LaunchedEffect
        backPressDispatcher?.addCallback {
            dismissSearchField()
            remove()
        }
    }

    Box(modifier) {
        TopAppBar(
            title = title,
            actions = actions,
            navigationIcon = navigationIcon,
            windowInsets = WindowInsets.statusBars,
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = colors.containerColor,
                navigationIconContentColor = colors.leadingIconColor,
                actionIconContentColor = colors.trailingIconsColor,
                titleContentColor = colors.titleColor
            )
        )
        AnimatedVisibility(
            visible = isSearchFieldVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.statusBarsPadding()
            ) {
                val focusRequester = remember { FocusRequester() }
                LaunchedEffect(Unit) {
                    coroutineContext.job.invokeOnCompletion {
                        focusRequester.requestFocus()
                    }
                }

                SearchTextField(
                    value = searchValue,
                    onValueChange = onSearchValueChange,
                    onClearText = onSearchValueClear,
                    placeholder = stringResource(R.string.entry_search_placeholder),
                    colors = TextFieldDefaults.searchTextFieldColors(
                        backgroundColor = colors.containerColor,
                        textColor = colors.titleColor,
                        cursorColor = colors.indicatorsColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .focusRequester(focusRequester)
                )
            }
        }
    }
}

data class SmallTopBarWithSearchColors(
    val containerColor: Color,
    val leadingIconColor: Color,
    val titleColor: Color,
    val trailingIconsColor: Color,
    val indicatorsColor: Color
) {
    companion object {
        @Composable
        fun default(
            containerColor: Color = MaterialTheme.colorScheme.surface,
            leadingIconColor: Color = MaterialTheme.colorScheme.onSurface,
            titleColor: Color = MaterialTheme.colorScheme.onSurface,
            trailingIconsColor: Color = MaterialTheme.colorScheme.onSurface,
            indicatorsColor: Color = MaterialTheme.colorScheme.primary
        ) = SmallTopBarWithSearchColors(
            containerColor, leadingIconColor, titleColor, trailingIconsColor, indicatorsColor
        )
    }
}
