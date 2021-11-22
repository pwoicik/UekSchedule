package com.github.pwoicik.uekschedule.database

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Class as JavaClass

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val apiRepository = ApiService.getInstance()
    private val roomRepository = AppDatabase.getInstance(application).scheduleDao()

    private val _isRefreshing = MutableStateFlow(true)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing

    private val _schedules: MutableState<List<Schedule>?> = mutableStateOf(null)
    val schedules: State<List<Schedule>?>
        get() = _schedules

    private val _classes: MutableState<List<Class>?> = mutableStateOf(null)
    val classes: State<List<Class>?>
        get() = _classes

    private fun fetchSchedule(groupId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val newSchedule = apiRepository.getSchedule(groupId)

            roomRepository.insertScheduleWithClasses(
                ScheduleWithClasses.fromModelSchedule(
                    newSchedule
                )
            )
        }
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _isRefreshing.emit(true)
            roomRepository.getAllSchedules().forEach { schedule ->
                fetchSchedule(schedule.groupID)
            }

            _schedules.value = roomRepository.getAllSchedules()
            _classes.value = roomRepository.getAllClasses()

            _isRefreshing.emit(false)
        }
    }

    init {
        refresh()
    }
}

class ScheduleViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: JavaClass<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            return ScheduleViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
