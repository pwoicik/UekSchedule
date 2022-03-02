package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.schedule

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.common.Resource
import com.github.pwoicik.uekschedule.feature_schedule.domain.use_case.ScheduleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ScheduleScreenViewModel @Inject constructor(
    private val scheduleUseCases: ScheduleUseCases
) : ViewModel() {

    private val _state = mutableStateOf(ScheduleScreenState())
    val state: State<ScheduleScreenState> = _state

    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow = _eventFlow.asSharedFlow()

    val timeFlow = flow {
        while (true) {
            val timeNow = LocalDateTime.now()
            emit(timeNow)
            delay((60 - timeNow.second) * 1000L)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), LocalDateTime.now())

    init {
        scheduleUseCases.getSavedGroupsCount().onEach { count ->
            val hasGroups = count > 0
            _state.value = state.value.copy(
                hasSavedGroups = hasGroups,
                entries = if (hasGroups) scheduleUseCases.getAllScheduleEntries() else emptyList()
            )
        }.launchIn(viewModelScope)

        refreshClasses()
    }

    private var refreshClassesJob: Job? = null

    fun refreshClasses() {
        refreshClassesJob?.cancel()
        refreshClassesJob = scheduleUseCases.refreshClasses()
            .onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isRefreshing = false)
                        _eventFlow.emit(UiEvent.ShowSnackbar)
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isRefreshing = true)
                        _eventFlow.emit(UiEvent.HideSnackbar)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isRefreshing = false)
                        _eventFlow.emit(UiEvent.HideSnackbar)
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun updateSearchResults(text: String) {
        _state.value = state.value.copy(
            searchText = text
        )
    }

    sealed class UiEvent {
        object ShowSnackbar : UiEvent()
        object HideSnackbar : UiEvent()
    }
}
