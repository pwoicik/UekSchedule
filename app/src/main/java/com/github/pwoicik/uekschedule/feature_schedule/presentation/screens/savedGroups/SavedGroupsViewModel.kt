package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.savedGroups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.feature_schedule.domain.use_case.ScheduleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedGroupsViewModel @Inject constructor(
    private val useCases: ScheduleUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(SavedGroupsState())
    val state = _state.asStateFlow()

    init {
        useCases.getSavedGroups().onEach { groups ->
            _state.update { it.copy(groups = groups) }
        }.launchIn(viewModelScope)
    }

    fun emit(event: SavedGroupsEvent) {
        when (event) {
            is SavedGroupsEvent.DeleteGroup -> viewModelScope.launch {
                val gwc = useCases.getGroupWithClasses(event.group)
                useCases.deleteGroup(event.group)
                _state.update { it.copy(userMessage = UserMessage.GroupDeleted(gwc)) }
            }
            is SavedGroupsEvent.UndoGroupDeletion -> viewModelScope.launch {
                useCases.saveGroupWithClasses(event.gwc)
            }
            SavedGroupsEvent.UserMessageSeen -> _state.update {
                it.copy(userMessage = UserMessage.None)
            }
            is SavedGroupsEvent.FavoriteGroup -> viewModelScope.launch {
                val isFavorite = event.group.isFavorite
                useCases.updateGroup(event.group.copy(isFavorite = !isFavorite))
            }
        }
    }
}
