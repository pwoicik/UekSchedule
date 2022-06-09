package com.github.pwoicik.uekschedule.features.groups.presentation.screens.savedGroups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.common.domain.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SavedGroupsViewModel @Inject constructor(
    private val repo: ScheduleRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SavedGroupsState())
    val state = _state.asStateFlow()

    init {
        repo.getSavedGroups().onEach { groups ->
            _state.update { it.copy(groups = groups) }
        }.launchIn(viewModelScope)
    }

    fun emit(event: SavedGroupsEvent) {
        when (event) {
            is SavedGroupsEvent.DeleteGroup -> viewModelScope.launch {
                val gwc = repo.getGroupWithClasses(event.group)
                repo.deleteGroup(event.group)
                _state.update { it.copy(userMessage = UserMessage.GroupDeleted(gwc)) }
            }
            is SavedGroupsEvent.UndoGroupDeletion -> viewModelScope.launch {
                repo.saveGroupWithClasses(event.gwc)
            }
            SavedGroupsEvent.UserMessageSeen -> _state.update {
                it.copy(userMessage = UserMessage.None)
            }
            is SavedGroupsEvent.FavoriteGroup -> viewModelScope.launch {
                val isFavorite = event.group.isFavorite
                repo.updateGroup(event.group.copy(isFavorite = !isFavorite))
            }
        }
    }
}
