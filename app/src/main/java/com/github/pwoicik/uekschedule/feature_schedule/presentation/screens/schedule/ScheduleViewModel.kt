package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.feature_schedule.common.timeFlow
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repo: ScheduleRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ScheduleState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    val timeFlow = timeFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), LocalDateTime.now())

    init {
        combine(
            repo.getSavedGroupsCount(),
            repo.getAllScheduleEntries()
        ) { count, entries ->
            _state.update { state ->
                val hasGroups = count > 0
                state.copy(
                    hasSavedGroups = hasGroups,
                    entries = if (hasGroups) entries else null
                )
            }
        }
            .conflate()
            .launchIn(viewModelScope)

        refreshData()
    }

    private var refreshJob: Job? = null
    private fun refreshData() {
        if (refreshJob?.isActive == true) return
        refreshJob = viewModelScope.launch {
            _eventFlow.emit(UiEvent.HideSnackbar)
            _state.update { state ->
                state.copy(isRefreshing = true)
            }

            repo.updateSchedules().onFailure {
                _eventFlow.emit(UiEvent.ShowErrorSnackbar)
            }
            _state.update { state ->
                state.copy(isRefreshing = false)
            }
        }
    }

    fun emit(event: ScheduleEvent) {
        when (event) {
            ScheduleEvent.FabClicked -> {
                viewModelScope.launch { _eventFlow.emit(UiEvent.ScrollToToday) }
            }
            is ScheduleEvent.SearchTextChanged -> {
                _state.update { state ->
                    state.copy(
                        searchValue = event.newValue.copy()
                    )
                }
            }
            ScheduleEvent.RefreshButtonClicked -> {
                refreshData()
            }
        }
    }

    sealed class UiEvent {
        object ShowErrorSnackbar : UiEvent()
        object HideSnackbar : UiEvent()
        object ScrollToToday : UiEvent()
    }
}
