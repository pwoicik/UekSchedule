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

    val scheduleEntries = scheduleUseCases.getAllScheduleEntries()

    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent>
        get() = _eventFlow

    private val _isUpdating = mutableStateOf(true)
    val isUpdating: State<Boolean>
        get() = _isUpdating

    val timeFlow = flow {
        while (true) {
            val timeNow = LocalDateTime.now()
            emit(timeNow)
            delay((60 - timeNow.second) * 1000L)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), LocalDateTime.now())

    init {
        updateClasses()
    }

    private var updateClassesJob: Job? = null

    fun updateClasses() {
        updateClassesJob?.cancel()
        updateClassesJob = scheduleUseCases.updateClasses()
            .onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        _isUpdating.value = false
                        _eventFlow.emit(UiEvent.ShowSnackbar)
                    }
                    is Resource.Loading -> {
                        _isUpdating.value = true
                    }
                    is Resource.Success -> {
                        _isUpdating.value = false
                        _eventFlow.emit(UiEvent.HideSnackbar)
                    }
                }
            }.launchIn(viewModelScope)
    }

    sealed class UiEvent {
        object ShowSnackbar : UiEvent()
        object HideSnackbar : UiEvent()
    }
}
