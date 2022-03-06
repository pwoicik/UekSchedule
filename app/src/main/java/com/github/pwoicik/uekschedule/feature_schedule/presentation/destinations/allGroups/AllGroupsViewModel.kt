package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.allGroups

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
        fetchJob = useCases.getAllGroups().onEach { response ->
            when (response) {
                is Resource.Error -> {
                    _state.update { state ->
                        state.copy(didTry = true, isLoading = false)
                    }
                    _eventFlow.emit(UiEvent.ShowErrorSnackbar)
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
                    useCases.addGroup(event.group)
                    _eventFlow.emit(UiEvent.ShowSavedGroupSnackbar(event.group))
                }
            }
            AllGroupsEvent.RetryGroupsFetch -> {
                if (fetchJob?.isActive == true) return
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.HideSnackbar)
                    fetchGroups()
                }
            }
        }
    }

    sealed class UiEvent {
        object ShowErrorSnackbar : UiEvent()
        object HideSnackbar : UiEvent()
        data class ShowSavedGroupSnackbar(val group: Group) : UiEvent()
    }
}
