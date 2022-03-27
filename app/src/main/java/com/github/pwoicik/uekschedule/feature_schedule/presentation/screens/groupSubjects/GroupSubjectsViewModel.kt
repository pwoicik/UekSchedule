package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.groupSubjects

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Subject
import com.github.pwoicik.uekschedule.feature_schedule.domain.use_case.ScheduleUseCases
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.destinations.GroupSubjectsScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupSubjectsViewModel @Inject constructor(
    private val useCases: ScheduleUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val groupId = GroupSubjectsScreenDestination.argsFrom(savedStateHandle).groupId

    val subjects = useCases.getAllSubjectsForGroup(groupId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun toggleSubjectIgnore(subject: Subject) {
        viewModelScope.launch {
            if (subject.isIgnored) {
                useCases.deleteSubjectFromIgnored(subject)
            } else {
                useCases.addSubjectToIgnored(subject)
            }
        }
    }
}
