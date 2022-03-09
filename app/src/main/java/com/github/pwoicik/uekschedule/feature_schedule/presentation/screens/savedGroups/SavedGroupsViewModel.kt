package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.savedGroups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.GroupWithClasses
import com.github.pwoicik.uekschedule.feature_schedule.domain.use_case.ScheduleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedGroupsViewModel @Inject constructor(
    private val useCases: ScheduleUseCases
) : ViewModel() {

    val savedGroups = useCases.getSavedGroups()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val savedActivities = useCases.getAllActivities()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun emit(event: SavedGroupsEvent) {
        when (event) {
            is SavedGroupsEvent.DeleteActivityButtonClicked -> {
                viewModelScope.launch {
                    useCases.deleteActivity(event.activity)
                    _eventFlow.emit(UiEvent.HideSnackbar)
                    _eventFlow.emit(UiEvent.ShowActivityDeletedSnackbar(event.activity))
                }
            }
            is SavedGroupsEvent.UndoActivityDeletion -> {
                viewModelScope.launch {
                    useCases.saveActivity(event.activity)
                    _eventFlow.emit(UiEvent.HideSnackbar)
                }
            }
            is SavedGroupsEvent.DeleteGroupButtonClicked -> {
                viewModelScope.launch {
                    val gwc = useCases.getGroupWithClasses(event.group)
                    useCases.deleteGroup(event.group)
                    _eventFlow.emit(UiEvent.HideSnackbar)
                    _eventFlow.emit(UiEvent.ShowGroupDeletedSnackbar(gwc))
                }
            }
            is SavedGroupsEvent.UndoGroupDeletion -> {
                viewModelScope.launch {
                    useCases.saveGroupWithClasses(event.gwc)
                    _eventFlow.emit(UiEvent.HideSnackbar)
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowActivityDeletedSnackbar(val activity: Activity) : UiEvent()
        data class ShowGroupDeletedSnackbar(val gwc: GroupWithClasses) : UiEvent()
        object HideSnackbar : UiEvent()
    }
}
