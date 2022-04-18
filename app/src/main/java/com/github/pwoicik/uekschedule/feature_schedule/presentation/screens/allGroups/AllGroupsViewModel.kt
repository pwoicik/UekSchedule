package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.allGroups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllGroupsViewModel @Inject constructor(
    private val repo: ScheduleRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AllGroupsState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        fetchGroups()
    }

    private var fetchJob: Job? = null
    private fun fetchGroups() {
        if (fetchJob?.isActive == true) return
        fetchJob = viewModelScope.launch {
            _state.update { state ->
                state.copy(isLoading = true)
            }
            _eventFlow.emit(UiEvent.HideSnackbar)

            val result = repo.getAllGroups().onFailure {
                _eventFlow.emit(
                    UiEvent.ShowErrorSnackbar(
                        AllGroupsEvent.RetryGroupsFetch
                    )
                )
            }
            _state.update { state ->
                state.copy(
                    didTry = true,
                    isLoading = false,
                    groups = result.getOrDefault(state.groups)
                )
            }
        }
    }

    fun emit(event: AllGroupsEvent) {
        when (event) {
            is AllGroupsEvent.SearchTextChanged -> {
                _state.update { state ->
                    state.copy(
                        searchValue = event.newValue
                    )
                }
            }
            is AllGroupsEvent.GroupSaveButtonClicked -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.HideSnackbar)

                    _state.update { state ->
                        state.copy(isSaving = true)
                    }
                    _eventFlow.emit(UiEvent.ShowSavingGroupSnackbar(event.group))

                    val result = repo.saveGroup(event.group)
                    _state.update { state ->
                        state.copy(isSaving = false)
                    }
                    _eventFlow.emit(
                        if (result.isSuccess) {
                            UiEvent.ShowSavedGroupSnackbar(event.group)
                        } else {
                            UiEvent.ShowErrorSnackbar(
                                AllGroupsEvent.GroupSaveButtonClicked(event.group)
                            )
                        }
                    )
                }
            }
            AllGroupsEvent.RetryGroupsFetch -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.HideSnackbar)
                    fetchGroups()
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowErrorSnackbar(val eventToRepeat: AllGroupsEvent) : UiEvent()
        object HideSnackbar : UiEvent()
        data class ShowSavingGroupSnackbar(val group: Group) : UiEvent()
        data class ShowSavedGroupSnackbar(val group: Group) : UiEvent()
    }
}
