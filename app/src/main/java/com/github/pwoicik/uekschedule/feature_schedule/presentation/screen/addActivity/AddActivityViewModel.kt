package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity
import com.github.pwoicik.uekschedule.feature_schedule.domain.use_case.ScheduleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class AddActivityViewModel @Inject constructor(
    private val scheduleUseCases: ScheduleUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state: MutableState<AddActivityScreenState?> = mutableStateOf(null)
    val state: State<AddActivityScreenState?>
        get() = _state

    private val activityId = savedStateHandle.get<Long>("activityId")!!

    init {
        if (activityId == -1L) {
            _state.value = AddActivityScreenState()
        } else {
            viewModelScope.launch {
                val activity = scheduleUseCases.getActivity(activityId)
                val repeatActivity = activity.repeatOnDaysOfWeek != null
                _state.value = AddActivityScreenState(
                    name = activity.name,
                    location = activity.location ?: "",
                    type = activity.type ?: "",
                    teacher = activity.teacher ?: "",
                    startDate = if (repeatActivity) null else activity.startDateTime.toLocalDate(),
                    startTime = activity.startDateTime.toLocalTime(),
                    durationMinutes = activity.durationMinutes.toString(),
                    repeatActivity = repeatActivity,
                    repeatOnDaysOfWeek = activity.repeatOnDaysOfWeek?.toSet() ?: emptySet()
                )
            }
        }
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = Int.MAX_VALUE)
    val eventFlow: SharedFlow<UiEvent>
        get() = _eventFlow

    fun event(event: AddActivityScreenEvent) {
        val state = _state.value!!
        when (event) {
            is AddActivityScreenEvent.NameChanged ->
                _state.value = state.copy(
                    name = event.value.trimStart()
                )
            is AddActivityScreenEvent.DurationMinutesChanged -> {
                if (event.value.isDigitsOnly()) {
                    _state.value = state.copy(
                        durationMinutes = event.value
                    )
                }
            }
            is AddActivityScreenEvent.LocationChanged ->
                _state.value = state.copy(
                    location = event.value.trimStart()
                )
            is AddActivityScreenEvent.RepeatOnDaysOfWeekChanged ->
                _state.value = state.copy(
                    repeatOnDaysOfWeek = event.value
                )
            is AddActivityScreenEvent.TeacherChanged ->
                _state.value = state.copy(
                    teacher = event.value.trimStart()
                )
            is AddActivityScreenEvent.TypeChanged ->
                _state.value = state.copy(
                    type = event.value.trimStart()
                )
            is AddActivityScreenEvent.RepeatActivityChanged ->
                _state.value = state.copy(
                    repeatActivity = !state.repeatActivity
                )
            is AddActivityScreenEvent.AddDayOfWeekToRepeat ->
                _state.value = state.copy(
                    repeatOnDaysOfWeek = state.repeatOnDaysOfWeek + event.value
                )
            is AddActivityScreenEvent.StartTimeChanged ->
                _state.value = state.copy(
                    startTime = event.value
                )
            is AddActivityScreenEvent.RemoveDayOfWeekToRepeat ->
                _state.value = state.copy(
                    repeatOnDaysOfWeek = state.repeatOnDaysOfWeek - event.value
                )
            is AddActivityScreenEvent.StartDateChanged ->
                _state.value = state.copy(
                    startDate = event.value
                )
            is AddActivityScreenEvent.SaveActivity -> {
                viewModelScope.launch {
                    if (state.name.isBlank()) {
                        _eventFlow.emit(UiEvent.ShowError)
                        return@launch
                    }
                    if (state.repeatActivity) {
                        if (state.repeatOnDaysOfWeek.isEmpty()) {
                            _eventFlow.emit(UiEvent.ShowError)
                            return@launch
                        }
                    } else {
                        if (state.startDate == null) {
                            _eventFlow.emit(UiEvent.ShowError)
                            return@launch
                        }
                    }

                    val startDateTime = if (state.repeatActivity) {
                        ZonedDateTime.of(
                            LocalDate.now(),
                            state.startTime,
                            ZoneId.systemDefault()
                        )
                    } else {
                        ZonedDateTime.of(
                            state.startDate,
                            state.startTime,
                            ZoneId.systemDefault()
                        )
                    }
                    scheduleUseCases.addActivity(
                        Activity(
                            id = if (activityId != -1L) activityId else 0,
                            name = state.name,
                            location = state.location.ifBlankThenNull(),
                            type = state.type.ifBlankThenNull(),
                            teacher = state.teacher.ifBlankThenNull(),
                            repeatOnDaysOfWeek = if (state.repeatActivity) {
                                state.repeatOnDaysOfWeek.toList()
                            } else null,
                            startDateTime = startDateTime,
                            durationMinutes = state.durationMinutes.toLong()
                        )
                    )

                    _eventFlow.emit(UiEvent.ActivitySaved)
                }
            }
        }
    }

    sealed class UiEvent {
        object ShowError : UiEvent()
        object ActivitySaved : UiEvent()
    }
}

private fun String.ifBlankThenNull() = ifBlank { null }
