package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.classes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.common.Resource
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Class
import com.github.pwoicik.uekschedule.feature_schedule.domain.use_case.ScheduleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ClassesViewModel @Inject constructor(
    private val scheduleUseCases: ScheduleUseCases
) : ViewModel() {

    private val _classes: MutableState<List<Class>?> = mutableStateOf(null)
    val classes: State<List<Class>?>
        get() = _classes

    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow(replay = Int.MAX_VALUE)
    val eventFlow: SharedFlow<UiEvent>
        get() = _eventFlow

    private val _isUpdating = mutableStateOf(true)
    val isUpdating: State<Boolean>
        get() = _isUpdating

    val timeFlow = flow {
        while (true) {
            delay(5_000)
            emit(ZonedDateTime.now())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ZonedDateTime.now())

    private var updateClassesJob: Job? = null

    init {
        updateClasses()

        scheduleUseCases.getAllClasses().onEach { classes ->
            _classes.value = classes
        }.launchIn(viewModelScope)
    }

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
