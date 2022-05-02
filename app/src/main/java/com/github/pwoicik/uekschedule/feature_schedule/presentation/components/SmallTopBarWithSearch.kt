package com.github.pwoicik.uekschedule.feature_schedule.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import com.github.pwoicik.uekschedule.R
import kotlinx.coroutines.job

@Composable
fun SmallTopBarWithSearch(
    title: @Composable () -> Unit,
    isSearchFieldVisible: Boolean,
    searchValue: TextFieldValue,
    onSearchValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    onSearchValueClear: () -> Unit = { onSearchValueChange(TextFieldValue()) },
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: SmallTopBarWithSearchColors = SmallTopBarWithSearchColors.default()
) {
    Surface(
        color = colors.containerColor,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .animateContentSize()
        ) {
            SmallTopAppBar(
                title = title,
                actions = actions,
                navigationIcon = navigationIcon,
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Transparent,
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
