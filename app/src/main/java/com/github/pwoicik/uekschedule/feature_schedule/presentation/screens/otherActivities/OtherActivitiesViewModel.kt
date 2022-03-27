package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.otherActivities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.feature_schedule.domain.use_case.ScheduleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherActivitiesViewModel @Inject constructor(
    private val useCases: ScheduleUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(OtherActivitiesState())
    val state = _state.asStateFlow()

    init {
        useCases.getAllActivities().onEach { activities ->
            _state.update { it.copy(activities = activities) }
        }.launchIn(viewModelScope)
    }

    fun emit(event: OtherActivitiesEvent) {
        when (event) {
            is OtherActivitiesEvent.DeleteActivity -> viewModelScope.launch {
                useCases.deleteActivity(event.activity)
                _state.update {
                    it.copy(userMessage = UserMessage.ActivityDeleted(event.activity))
                }
            }
            is OtherActivitiesEvent.UndoActivityDeletion -> viewModelScope.launch {
                useCases.saveActivity(event.activity)
            }
            OtherActivitiesEvent.UserMessageSeen -> _state.update {
                it.copy(userMessage = UserMessage.None)
            }
        }
    }
}
