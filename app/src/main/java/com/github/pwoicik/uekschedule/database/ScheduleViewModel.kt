package com.github.pwoicik.uekschedule.database

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.api.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Class as JavaClass

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val apiRepository = ApiRepository()
    private val roomRepository = AppDatabase.getInstance(application).scheduleDao()

    private val _isRefreshing = MutableStateFlow(true)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing

    val groups = roomRepository
        .getAllGroups()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val classes = roomRepository
        .getAllClasses()
        .map {
            it.sortedBy(Class::startDateTime)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val _availableGroups: MutableState<List<Group>?> = mutableStateOf(null)
    val availableGroups: State<List<Group>?>
        get() {
            if (_availableGroups.value == null)
                viewModelScope.launch(Dispatchers.IO) {
                    _availableGroups.value = apiRepository.getGroups()
                }

            return _availableGroups
        }

    private fun fetchSchedule(groupId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val newSchedule = apiRepository.getSchedule(groupId)
            roomRepository.insertGroupWithClasses(newSchedule)
        }
    }

    fun addSchedules(groups: List<Group>) =
        viewModelScope.launch(Dispatchers.IO) {
            for (group in groups) {
                val schedule = apiRepository.getSchedule(group.id)
                roomRepository.insertGroupWithClasses(schedule)
            }
        }

    fun deleteGroup(groupId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.deleteGroup(groupId)
        }
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _isRefreshing.emit(true)

            groups.value?.forEach { group ->
                fetchSchedule(group.id)
            }

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
