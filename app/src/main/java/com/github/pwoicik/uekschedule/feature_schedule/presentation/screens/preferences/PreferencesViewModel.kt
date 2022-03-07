package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.feature_schedule.data.preferences.PreferencesManager
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferences: PreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow(PreferencesState())
    val state = _state.asStateFlow()

    init {
        preferences.theme.onEach { theme ->
            _state.update { state ->
                state.copy(
                    isDynamicTheme = theme.isDynamic,
                    theme = theme
                )
            }
        }.launchIn(viewModelScope)
    }

    fun setDynamicTheme(isDynamic: Boolean) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    isDynamicTheme = isDynamic,
                    theme = if (isDynamic) Preferences.Defaults.DYNAMIC_THEME else Preferences.Defaults.THEME
                )
            }
            preferences.setTheme(state.value.theme)
        }
    }

    fun setTheme(theme: Preferences.Theme) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(theme = theme)
            }
            preferences.setTheme(state.value.theme)
        }
    }
}
