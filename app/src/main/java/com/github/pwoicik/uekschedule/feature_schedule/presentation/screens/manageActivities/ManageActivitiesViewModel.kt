package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.manageActivities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity
import com.github.pwoicik.uekschedule.feature_schedule.domain.use_case.ScheduleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageActivitiesViewModel @Inject constructor(
    private val scheduleUseCases: ScheduleUseCases
) : ViewModel() {

    val activities = scheduleUseCases.getAllActivities()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun deleteActivity(activity: Activity) {
        viewModelScope.launch {
            scheduleUseCases.deleteActivity(activity)
        }
    }
}
