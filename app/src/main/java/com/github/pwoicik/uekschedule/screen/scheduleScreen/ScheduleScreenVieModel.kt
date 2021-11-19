package com.github.pwoicik.uekschedule.screen.scheduleScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.api.ApiService
import com.github.pwoicik.uekschedule.model.Schedule
import kotlinx.coroutines.launch

class ScheduleScreenVieModel : ViewModel() {
    private val repository = ApiService.getInstance()
    private lateinit var _schedule: MutableState<Schedule>

    fun getSchedule(groupId: String): MutableState<Schedule> {
        if (!::_schedule.isInitialized) {
            _schedule = mutableStateOf(Schedule())
            viewModelScope.launch {
                _schedule.value = repository.getSchedule(groupId)
                _schedule.value.classes?.forEach { clazz ->
                    clazz.endTime = clazz.endTime.dropLast(6)
                }
            }
        }

        return _schedule
    }
}
