package com.github.pwoicik.uekschedule.features.search.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.common.domain.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
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
                Timber.d(state.value.toString())
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
            Timber.d(state.value.toString())
        }
    }

    private var filterJob: Job? = null
    fun emit(event: SearchEvent) {
        when (event) {
            is SearchEvent.SearchTextChanged -> {
                _state.update { state ->
                    state.copy(
                        searchableSchedulablesState = state.searchableSchedulablesState.copy(
                            searchValue = event.newValue
                        )
                    )
                }

                filterJob?.cancel()
                filterJob = viewModelScope.launch {
                    delay(0.5.seconds)
                    Timber.d("xyz")
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
            is SearchEvent.SchedulableSaveButtonClicked -> viewModelScope.launch {
                _state.update { state ->
                    state.copy(
                        searchableSchedulablesState = state.searchableSchedulablesState.copy(
                            isSaving = true
                        ),
                        userMessage = UserMessage.SavingSchedulable(event.schedulable)
                    )
                }

                val result = repo.saveSchedulable(event.schedulable)
                _state.update { state ->
                    state.copy(
                        searchableSchedulablesState = state.searchableSchedulablesState.copy(
                            isSaving = false
                        ),
                        userMessage = when (result.isSuccess) {
                            true -> UserMessage.SavedSchedulable(event.schedulable)
                            false -> UserMessage.Error(
                                SearchEvent.SchedulableSaveButtonClicked(event.schedulable)
                            )
                        }
                    )
                }
            }
            SearchEvent.RetrySchedulablesFetch -> fetchSchedulables()
            is SearchEvent.PageChanged -> {
                _state.update {
                    it.copy(currentPage = event.targetScreen)
                }
                fetchSchedulables()
            }
            SearchEvent.UserMessageSeen -> _state.update {
                it.copy(userMessage = null)
            }
        }
    }
}
