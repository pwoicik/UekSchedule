package com.github.pwoicik.uekschedule.screen.scheduleScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.api.ApiService
import com.github.pwoicik.uekschedule.model.Class
import com.github.pwoicik.uekschedule.model.Schedule
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

internal class ScheduleScreenVieModel : ViewModel() {
    private val repository = ApiService.getInstance()

    private val _isRefreshing = mutableStateOf(true)
    val isRefreshing: State<Boolean>
        get() = _isRefreshing

    private lateinit var _schedule: MutableState<Schedule>

    fun getSchedule(): State<Schedule> {
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

    fun getSchedule(groupId: String): State<Schedule> {
        if (!::_schedule.isInitialized) {
            _schedule = mutableStateOf(Schedule())
            viewModelScope.launch {
                _isRefreshing.value = true
                _schedule.value = repository.getSchedule(groupId)
                _schedule.value.classes?.forEach { clazz ->
                    clazz._endTime = clazz._endTime.dropLast(6)
                }
                _isRefreshing.value = false
            }
        }

        return _schedule
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _schedule.value = repository.getSchedule(_schedule.value.groupId)
            _schedule.value.classes?.forEach { clazz ->
                clazz._endTime = clazz._endTime.dropLast(6)
            }
            _isRefreshing.value = false
        }
    }
}
