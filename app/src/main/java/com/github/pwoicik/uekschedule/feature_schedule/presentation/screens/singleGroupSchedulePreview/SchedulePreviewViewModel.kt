package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.singleGroupSchedulePreview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.feature_schedule.common.timeFlow
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.SingleGroupSchedulePreviewScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SchedulePreviewViewModel @Inject constructor(
    private val repo: ScheduleRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs = SingleGroupSchedulePreviewScreenDestination.argsFrom(savedStateHandle)

    private val _state = MutableStateFlow(SchedulePreviewState(groupName = navArgs.groupName))
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    val timeFlow = timeFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), LocalDateTime.now())

    init {
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

            val result = repo.fetchSchedule(navArgs.groupId).onFailure {
                _eventFlow.emit(UiEvent.ShowErrorSnackbar)
            }
            _state.update { state ->
                state.copy(
                    didTry = true,
                    isRefreshing = false,
                    entries = result.getOrDefault(state.entries)
                )
            }
        }
    }

    fun emit(event: SchedulePreviewEvent) {
        when (event) {
            SchedulePreviewEvent.FabClicked -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ScrollToToday)
                }
            }
            SchedulePreviewEvent.RefreshButtonClicked -> {
                refreshData()
            }
            is SchedulePreviewEvent.SearchTextChanged -> {
                _state.update { state ->
                    state.copy(searchValue = event.newValue)
                }
            }
        }
    }

    sealed class UiEvent {
        object ShowErrorSnackbar : UiEvent()
        object HideSnackbar : UiEvent()
        object ScrollToToday : UiEvent()
    }
}
