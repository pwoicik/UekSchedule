package com.github.pwoicik.uekschedule.features.search.presentation.screens.search

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.common.domain.ScheduleRepository
import com.github.pwoicik.uekschedule.domain.model.Schedulable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    private val repo: ScheduleRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    init {
        fetchSchedulables()
    }

    private var fetchJob: Job? = null
    private fun fetchSchedulables() {
        if (fetchJob?.isActive == true) return
        fetchJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    searchableSchedulablesState = SearchableSchedulablesState()
                )
            }

            val result = repo.getAllSchedulables(state.value.currentSchedulableType).onFailure {
                _state.update {
                    it.copy(
                        searchableSchedulablesState = it.searchableSchedulablesState.copy(
                            didTry = true,
                            isLoading = false
                        ),
                        userMessage = UserMessage.Error(SearchEvent.RetrySchedulablesFetch)
                    )
                }
                return@launch
            }
            _state.update { state ->
                val sss = state.searchableSchedulablesState
                val schedulables = result.getOrDefault(sss.items)
                state.copy(
                    searchableSchedulablesState = sss.copy(
                        didTry = true,
                        isLoading = false,
                        items = schedulables,
                        filteredItems = schedulables?.filter {
                            it.name.contains(sss.searchValue.text, ignoreCase = true)
                        } ?: emptyList()
                    )
                )
            }
        }
    }

    private var filterJob: Job? = null
    private fun filterSchedulables() {
        filterJob?.cancel()
        filterJob = viewModelScope.launch {
            delay(0.5.seconds)
            _state.update { state ->
                val sss = state.searchableSchedulablesState
                state.copy(
                    searchableSchedulablesState = sss.copy(
                        filteredItems = sss.items?.filter {
                            it.name.contains(
                                sss.searchValue.text,
                                ignoreCase = true
                            )
                        } ?: emptyList()
                    )
                )
            }
        }
    }

    private fun onSearchTextChanged(value: TextFieldValue) {
        _state.update { state ->
            state.copy(
                searchableSchedulablesState = state.searchableSchedulablesState.copy(
                    searchValue = value
                )
            )
        }

        filterSchedulables()
    }

    private suspend fun onSaveButtonClick(schedulable: Schedulable) {
        _state.update { state ->
            state.copy(
                searchableSchedulablesState = state.searchableSchedulablesState.copy(
                    isSaving = true
                ),
                userMessage = UserMessage.SavingSchedulable(schedulable)
            )
        }

        val result = repo.saveSchedulable(schedulable)
        _state.update { state ->
            state.copy(
                searchableSchedulablesState = state.searchableSchedulablesState.copy(
                    isSaving = false
                ),
                userMessage = when (result.isSuccess) {
                    true -> UserMessage.SavedSchedulable(schedulable)
                    false -> UserMessage.Error(
                        SearchEvent.SchedulableSaveButtonClicked(schedulable)
                    )
                }
            )
        }
    }

    private fun onPageChange(page: SearchPages) {
        _state.update {
            it.copy(currentPage = page)
        }
        fetchSchedulables()

    }

    fun emit(event: SearchEvent) {
        when (event) {
            is SearchEvent.SearchTextChanged -> onSearchTextChanged(event.newValue)
            is SearchEvent.SchedulableSaveButtonClicked -> viewModelScope.launch {
                onSaveButtonClick(event.schedulable)
            }
            SearchEvent.RetrySchedulablesFetch -> fetchSchedulables()
            is SearchEvent.PageChanged -> onPageChange(event.targetPage)
            SearchEvent.UserMessageSeen -> _state.update {
                it.copy(userMessage = null)
            }
        }
    }
}
