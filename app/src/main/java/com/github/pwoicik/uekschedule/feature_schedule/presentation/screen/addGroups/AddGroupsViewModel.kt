package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addGroups

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.common.Resource
import com.github.pwoicik.uekschedule.feature_schedule.domain.use_case.ScheduleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class AddGroupsViewModel @Inject constructor(
    private val scheduleUseCases: ScheduleUseCases
) : ViewModel() {

    val savedGroups = scheduleUseCases.getSavedGroupsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _availableGroups = mutableStateOf(AvailableGroupsState(isLoading = true))
    val availableGroups: State<AvailableGroupsState>
        get() = _availableGroups

    private val _addGroupsState = mutableStateOf(AddGroupsState())
    val addGroupsState: State<AddGroupsState>
        get() = _addGroupsState

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = Int.MAX_VALUE)
    val eventFlow: SharedFlow<UiEvent>
        get() = _eventFlow

    private var getAvailableGroupsJob: Job? = null
    private var saveSelectedGroupsJob: Job? = null

    init {
        getAvailableGroups()
    }

    fun event(event: AddGroupsEvent) {
        when (event) {
            is AddGroupsEvent.SelectGroup -> {
                val selectedGroups = _addGroupsState.value.selectedGroups + event.group
                _addGroupsState.value = _addGroupsState.value.copy(
                    selectedGroups = selectedGroups,
                    filteredGroups = _availableGroups.value.groups.filter { group ->
                        group !in selectedGroups
                    }
                )
            }
            is AddGroupsEvent.UnselectGroup -> {
                val selectedGroups = _addGroupsState.value.selectedGroups - event.group
                _addGroupsState.value = _addGroupsState.value.copy(
                    selectedGroups = selectedGroups,
                    filteredGroups = _availableGroups.value.groups.filter { group ->
                        group !in selectedGroups
                    }
                )
            }
            is AddGroupsEvent.SearchTextChange -> {
                _addGroupsState.value = _addGroupsState.value.copy(
                    searchText = event.text,
                    filteredGroups = _availableGroups.value.groups.filter { group ->
                        event.text.lowercase() in group.name.lowercase()
                    }
                )
            }
            is AddGroupsEvent.ClearSelectedGroups -> {
                _addGroupsState.value = _addGroupsState.value.copy(
                    selectedGroups = emptyList(),
                    filteredGroups = _availableGroups.value.groups.filter { group ->
                        _addGroupsState.value.searchText.lowercase() in group.name.lowercase()
                    }
                )
            }
            is AddGroupsEvent.SaveSelectedGroups,
            is AddGroupsEvent.RetrySaveSelectedGroups -> {
                saveSelectedGroups()
            }
            is AddGroupsEvent.RetryGetAvailableGroups -> {
                getAvailableGroups()
            }
        }
    }

    private fun getAvailableGroups() {
        getAvailableGroupsJob?.cancel()
        getAvailableGroupsJob = scheduleUseCases
            .getAllGroups()
            .onEach { groups ->
                when (groups) {
                    is Resource.Loading ->
                        _availableGroups.value = _availableGroups.value.copy(
                            isLoading = true
                        )
                    is Resource.Success -> {
                        _availableGroups.value = _availableGroups.value.copy(
                            isLoading = false,
                            isError = false,
                            groups = groups.data!!
                        )
                        _addGroupsState.value = _addGroupsState.value.copy(
                            filteredGroups = groups.data
                        )
                    }
                    is Resource.Error -> {
                        _availableGroups.value = _availableGroups.value.copy(
                            isError = true,
                            isLoading = false,
                        )
                        _eventFlow.emit(UiEvent.ShowSnackbar)
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun saveSelectedGroups() {
        saveSelectedGroupsJob?.cancel()
        saveSelectedGroupsJob = scheduleUseCases
            .addGroups(_addGroupsState.value.selectedGroups)
            .onEach { res ->
                when (res) {
                    is Resource.Loading -> {
                        _addGroupsState.value = _addGroupsState.value.copy(
                            isSaving = true
                        )
                    }
                    is Resource.Success -> {
                        _eventFlow.emit(UiEvent.AddedGroups)
                        _eventFlow.emit(UiEvent.HideSnackbar)
                    }
                    is Resource.Error -> {
                        _addGroupsState.value = _addGroupsState.value.copy(
                            isSaving = false,
                            isSavingError = true
                        )
                        _eventFlow.emit(UiEvent.ShowSnackbar)
                    }
                }
            }.launchIn(viewModelScope)
    }

    sealed class UiEvent {
        object ShowSnackbar : UiEvent()
        object HideSnackbar : UiEvent()
        object AddedGroups : UiEvent()
    }
}
