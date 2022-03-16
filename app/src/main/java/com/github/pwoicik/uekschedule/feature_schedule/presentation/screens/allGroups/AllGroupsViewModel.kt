package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.allGroups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.common.Resource
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import com.github.pwoicik.uekschedule.feature_schedule.domain.use_case.ScheduleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllGroupsViewModel @Inject constructor(
    private val useCases: ScheduleUseCases
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
        fetchJob = useCases.getAllGroups().onEach { response ->
            when (response) {
                is Resource.Error -> {
                    _state.update { state ->
                        state.copy(didTry = true, isLoading = false)
                    }
                    _eventFlow.emit(
                        UiEvent.ShowErrorSnackbar(
                            AllGroupsEvent.RetryGroupsFetch
                        )
                    )
                }
                is Resource.Loading -> {
                    _state.update { state ->
                        state.copy(isLoading = true)
                    }
                    _eventFlow.emit(UiEvent.HideSnackbar)
                }
                is Resource.Success -> {
                    _state.update { state ->
                        state.copy(
                            didTry = true,
                            isLoading = false,
                            groups = response.data
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
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
                    useCases.saveGroup(event.group).collect { result ->
                        when (result) {
                            is Resource.Error -> {
                                _state.update { state ->
                                    state.copy(isSaving = false)
                                }
                                _eventFlow.emit(
                                    UiEvent.ShowErrorSnackbar(
                                        AllGroupsEvent.GroupSaveButtonClicked(event.group)
                                    )
                                )
                            }
                            is Resource.Loading -> {
                                _state.update { state ->
                                    state.copy(isSaving = true)
                                }
                                _eventFlow.emit(UiEvent.ShowSavingGroupSnackbar(event.group))
                            }
                            is Resource.Success -> {
                                _state.update { state ->
                                    state.copy(isSaving = false)
                                }
                                _eventFlow.emit(UiEvent.ShowSavedGroupSnackbar(event.group))
                            }
                        }
                    }
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
