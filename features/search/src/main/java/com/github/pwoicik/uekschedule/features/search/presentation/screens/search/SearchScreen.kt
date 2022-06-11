package com.github.pwoicik.uekschedule.features.search.presentation.screens.search

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.common.R
import com.github.pwoicik.uekschedule.domain.model.Schedulable
import com.github.pwoicik.uekschedule.features.search.presentation.screens.search.components.SearchItem
import com.github.pwoicik.uekschedule.features.search.presentation.screens.search.components.SearchItemsColumn
import com.github.pwoicik.uekschedule.features.search.presentation.screens.search.components.SearchScaffold
import com.github.pwoicik.uekschedule.presentation.components.SnackbarVisualsWithError
import com.github.pwoicik.uekschedule.presentation.components.SnackbarVisualsWithLoading
import com.github.pwoicik.uekschedule.presentation.components.SnackbarVisualsWithSuccess
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

interface SearchNavigator {
    fun openSchedulePreview(schedulable: Schedulable)
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SearchScreen(
    navigator: SearchNavigator
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val resources = LocalContext.current.resources
    LaunchedEffect(state.userMessage) {
        when (val msg = state.userMessage) {
            is UserMessage.Error -> launch {
                val result = snackbarHostState.showSnackbar(
                    SnackbarVisualsWithError(
                        message = resources.getString(R.string.couldnt_connect),
                        actionLabel = resources.getString(R.string.retry)
                    )
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.emit(msg.eventToRepeat)
                }
                viewModel.emit(SearchEvent.UserMessageSeen)
            }
            is UserMessage.SavedSchedulable -> launch {
                snackbarHostState.showSnackbar(
                    SnackbarVisualsWithSuccess(
                        message = resources.getString(
                            R.string.group_saved,
                            msg.schedulable.name
                        )
                    )
                )
            }
            is UserMessage.SavingSchedulable -> launch {
                snackbarHostState.showSnackbar(
                    SnackbarVisualsWithLoading(
                        message = resources.getString(
                            R.string.saving_group,
                            msg.schedulable.name
                        )
                    )
                )
            }
            null -> Unit
        }
    }

    SearchScaffold(
        searchableSchedulablesState = state.searchableSchedulablesState,
        onSearchValueChange = { viewModel.emit(SearchEvent.SearchTextChanged(it)) },
        currentPage = state.currentPage,
        onPageChange = { viewModel.emit(SearchEvent.PageChanged(it)) },
        focus = state.searchableSchedulablesState.items != null,
        snackbarHostState = snackbarHostState
    ) {
        val sss = state.searchableSchedulablesState

        val keyboardController = LocalSoftwareKeyboardController.current
        SearchItemsColumn {
            items(sss.filteredItems) { schedulable ->
                SearchItem(
                    itemName = schedulable.name,
                    isItemButtonEnabled = !sss.isSaving,
                    onItemClick = {
                        keyboardController?.hide()
                        navigator.openSchedulePreview(schedulable)
                    },
                    onSaveItemClick = {
                        viewModel.emit(
                            SearchEvent.SchedulableSaveButtonClicked(schedulable)
                        )
                    }
                )
            }
        }
    }
}
