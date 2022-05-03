package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.otherActivities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.common.domain.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherActivitiesViewModel @Inject constructor(
    private val repo: ScheduleRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OtherActivitiesState())
    val state = _state.asStateFlow()

    init {
        repo.getAllActivities().onEach { activities ->
            _state.update { it.copy(activities = activities) }
        }.launchIn(viewModelScope)
    }

    fun emit(event: OtherActivitiesEvent) {
        when (event) {
            is OtherActivitiesEvent.DeleteActivity -> viewModelScope.launch {
                repo.deleteActivity(event.activity)
                _state.update {
                    it.copy(userMessage = UserMessage.ActivityDeleted(event.activity))
                }
            }
            is OtherActivitiesEvent.UndoActivityDeletion -> viewModelScope.launch {
                repo.saveActivity(event.activity)
            }
            OtherActivitiesEvent.UserMessageSeen -> _state.update {
                it.copy(userMessage = UserMessage.None)
            }
        }
    }
}
