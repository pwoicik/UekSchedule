package com.github.pwoicik.uekschedule.features.groups.presentation.screens.groupSubjects

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.common.domain.ScheduleRepository
import com.github.pwoicik.uekschedule.domain.model.Subject
import com.github.pwoicik.uekschedule.features.groups.presentation.screens.groupSubjects.destinations.GroupSubjectsScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GroupSubjectsViewModel @Inject constructor(
    private val repo: ScheduleRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val groupId = GroupSubjectsScreenDestination.argsFrom(savedStateHandle).groupId

    val subjects = repo.getAllSubjectsForGroup(groupId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun toggleSubjectIgnore(subject: Subject) {
        viewModelScope.launch {
            if (subject.isIgnored) {
                repo.deleteSubjectFromIgnored(subject)
            } else {
                repo.addSubjectToIgnored(subject)
            }
        }
    }
}
