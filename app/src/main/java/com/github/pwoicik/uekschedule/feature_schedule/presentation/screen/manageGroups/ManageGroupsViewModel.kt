package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.manageGroups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Group
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

    val savedGroups = scheduleUseCases.getSavedGroupsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun deleteGroup(group: Group) {
        viewModelScope.launch {
            scheduleUseCases.deleteGroup(group)
        }
    }
}
