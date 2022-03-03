package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.manageGroups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import com.github.pwoicik.uekschedule.feature_schedule.domain.use_case.ScheduleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageGroupsViewModel @Inject constructor(
    private val scheduleUseCases: ScheduleUseCases
) : ViewModel() {

    val savedGroups = scheduleUseCases.getSavedGroups()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun deleteGroup(group: Group) {
        viewModelScope.launch {
            scheduleUseCases.deleteGroup(group)
        }
    }
}
