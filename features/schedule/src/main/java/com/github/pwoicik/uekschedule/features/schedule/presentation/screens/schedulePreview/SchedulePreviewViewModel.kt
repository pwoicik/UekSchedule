package com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedulePreview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.common.domain.ScheduleRepository
import com.github.pwoicik.uekschedule.common.util.timeFlow
import com.github.pwoicik.uekschedule.features.schedule.presentation.components.filterEntries
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.destinations.SchedulePreviewScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class SchedulePreviewViewModel @Inject constructor(
    private val repo: ScheduleRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs = SchedulePreviewScreenDestination.argsFrom(savedStateHandle)

    private val _state = MutableStateFlow(SchedulePreviewState(groupName = navArgs.schedulableName))
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    val timeFlow = timeFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), LocalDateTime.now())

    init {
        refreshData()
    }

    private var refreshJob: Job? = null
    private fun refreshData() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _eventFlow.emit(UiEvent.HideSnackbar)
            _state.update {
                it.copy(isRefreshing = true)
            }

            val result =
                repo.fetchSchedule(navArgs.schedulableId, navArgs.schedulableType).onFailure {
                    _eventFlow.emit(UiEvent.ShowErrorSnackbar)
                }
            _state.update {
                val entries = result.getOrDefault(it.entries ?: emptyList())
                it.copy(
                    didTry = true,
                    isRefreshing = false,
                    entries = entries,
                    filteredEntries = entries.filterEntries(it.searchValue.text)
                )
            }
        }
    }

    private var filterJob: Job? = null
    fun emit(event: SchedulePreviewEvent) {
        when (event) {
            SchedulePreviewEvent.FabClicked -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ScrollToToday)
                }
            }
            SchedulePreviewEvent.RefreshButtonClicked -> {
                refreshData()
            }
            is SchedulePreviewEvent.SearchTextChanged -> {
                _state.update {
                    it.copy(searchValue = event.newValue)
                }

                filterJob?.cancel()
                filterJob = viewModelScope.launch {
                    delay(0.5.seconds)
                    _state.update {
                        it.copy(
                            filteredEntries = it.entries
                                ?.filterEntries(it.searchValue.text)
                                ?: emptyMap()
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        object ShowErrorSnackbar : UiEvent()
        object HideSnackbar : UiEvent()
        object ScrollToToday : UiEvent()
    }
}
