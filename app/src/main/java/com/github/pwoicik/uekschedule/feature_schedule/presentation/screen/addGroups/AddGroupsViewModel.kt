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

    val savedGroups = scheduleUseCases.getSavedGroups()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = mutableStateOf(AddGroupsScreenState())
    val state: State<AddGroupsScreenState>
        get() = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = Int.MAX_VALUE)
    val eventFlow: SharedFlow<UiEvent>
        get() = _eventFlow

    private var getAvailableGroupsJob: Job? = null
    private var saveSelectedGroupsJob: Job? = null

    init {
        getAvailableGroups()
    }

    fun event(event: AddGroupsEvent) {
        val state = _state.value
        when (event) {
            is AddGroupsEvent.SelectGroup -> {
                _state.value = state.copy(
                    selectedGroups = state.selectedGroups + event.group
                )
            }
            is AddGroupsEvent.UnselectGroup -> {
                val selectedGroups = state.selectedGroups - event.group
                _state.value = state.copy(
                    selectedGroups = selectedGroups
                )
            }
            is AddGroupsEvent.SearchTextChange -> {
                _state.value = state.copy(
                    searchText = event.text
                )
            }
            is AddGroupsEvent.ClearSelectedGroups -> {
                _state.value = state.copy(
                    selectedGroups = emptyList()
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
                val state = _state.value
                when (groups) {
                    is Resource.Loading ->
                        _state.value = state.copy(
                            availableGroupsState = state.availableGroupsState.copy(
                                isLoading = true
                            )
                        )
                    is Resource.Success -> {
                        _state.value = state.copy(
                            availableGroupsState = state.availableGroupsState.copy(
                                isLoading = false,
                                isError = false,
                                groups = groups.data!!
                            )
                        )
                    }
                    is Resource.Error -> {
                        _state.value = state.copy(
                            availableGroupsState = state.availableGroupsState.copy(
                                isError = true,
                                isLoading = false,
                            )
                        )
                        _eventFlow.emit(UiEvent.ShowSnackbar)
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun saveSelectedGroups() {
        saveSelectedGroupsJob?.cancel()
        saveSelectedGroupsJob = scheduleUseCases
            .addGroups(_state.value.selectedGroups)
            .onEach { res ->
                when (res) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isSaving = true
                        )
                    }
                    is Resource.Success -> {
                        _eventFlow.emit(UiEvent.AddedGroups)
                        _eventFlow.emit(UiEvent.HideSnackbar)
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
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
