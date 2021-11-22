package com.github.pwoicik.uekschedule.screen.scheduleScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.api.ApiService
import com.github.pwoicik.uekschedule.model.Class
import com.github.pwoicik.uekschedule.model.Schedule
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class ScheduleScreenVieModel : ViewModel() {
    private val repository = ApiService.getInstance()
    private lateinit var _schedule: MutableState<Schedule>

    fun getSchedule(): MutableState<Schedule> {
        return mutableStateOf(
            Schedule(
                classes = listOf(
                    Class(
                        "Administrowanie sieciami komputerowymi",
                        LocalDate.now().toString(),
                        LocalTime.now().minusMinutes(30L).toString(),
                        LocalTime.now().toString(),
                        "ćwiczenia",
                        null,
                        "mgr Jakub Kanclerz",
                        "Paw. A 014 lab. Win 8.1, Office16"
                    ),
                    Class(
                        "Administrowanie sieciami komputerowymi",
                        LocalDate.now().toString(),
                        LocalTime.now().toString(),
                        LocalTime.now().plusMinutes(30L).toString(),
                        "ćwiczenia",
                        null,
                        "mgr Jakub Kanclerz",
                        "Paw. A 014 lab. Win 8.1, Office16"
                    ),
                    Class(
                        "Administrowanie sieciami komputerowymi",
                        LocalDate.now().toString(),
                        LocalTime.now().plusMinutes(60L).toString(),
                        LocalTime.now().plusMinutes(180L).toString(),
                        "ćwiczenia",
                        null,
                        "mgr Jakub Kanclerz",
                        "Paw. A 014 lab. Win 8.1, Office16"
                    )
                )
            )
        )
    }

    fun getSchedule(groupId: String): MutableState<Schedule> {
        if (!::_schedule.isInitialized) {
            _schedule = mutableStateOf(Schedule())
            viewModelScope.launch {
                _schedule.value = repository.getSchedule(groupId)
                _schedule.value.classes?.forEach { clazz ->
                    clazz._endTime = clazz._endTime.dropLast(6)
                }
            }
        }

        return _schedule
    }
}
