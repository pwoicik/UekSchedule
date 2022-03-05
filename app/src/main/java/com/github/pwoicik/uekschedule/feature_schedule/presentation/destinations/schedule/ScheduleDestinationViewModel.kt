package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.common.Resource
import com.github.pwoicik.uekschedule.feature_schedule.common.timeFlow
import com.github.pwoicik.uekschedule.feature_schedule.domain.use_case.ScheduleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ScheduleDestinationViewModel @Inject constructor(
    private val useCases: ScheduleUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ScheduleDestinationState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    val timeFlow = timeFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), LocalDateTime.now())

    init {
        combine(
            useCases.getSavedGroupsCount(),
            useCases.getAllScheduleEntries()
        ) { count, entries ->
            _state.update { state ->
                if (state.entries == null) {
                    refreshData()
                }
                val hasGroups = count > 0
                state.copy(
                    hasSavedGroups = hasGroups,
                    entries = if (hasGroups) entries else null
                )
            }
        }.launchIn(viewModelScope)
    }

    private var refreshJob: Job? = null
    private fun refreshData() {
        if (refreshJob?.isActive == true) return
        refreshJob = useCases.refreshClasses().onEach { response ->
            when (response) {
                is Resource.Error -> {
                    _state.update { state ->
                        state.copy(isRefreshing = false)
                    }
                }
                is Resource.Loading -> {
                    _state.update { state ->
                        state.copy(isRefreshing = true)
                    }
                }
                is Resource.Success -> {
                    _state.update { state ->
                        state.copy(isRefreshing = false)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun emit(event: ScheduleDestinationEvent) {
        when (event) {
            ScheduleDestinationEvent.FabClicked -> {
                viewModelScope.launch { _eventFlow.emit(UiEvent.ScrollToToday) }
            }
            is ScheduleDestinationEvent.SearchTextChanged -> {
                _state.update { state ->
                    state.copy(
                        searchValue = event.newValue.copy()
                    )
                }
            }
            ScheduleDestinationEvent.RefreshButtonClicked -> {
                refreshData()
            }
        }
    }

    sealed class UiEvent {
        object ShowErrorSnackbar : UiEvent()
        object ScrollToToday : UiEvent()
    }
}
