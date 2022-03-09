package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwoicik.uekschedule.feature_schedule.data.preferences.PreferencesManager
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
                    theme = ThemePreference.fromPreferencesTheme(theme)
                )
            }
        }.launchIn(viewModelScope)
    }

    fun setDynamicTheme(isDynamic: Boolean) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(isDynamicTheme = isDynamic)
            }
            applyTheme()
        }
    }

    fun setTheme(theme: ThemePreference) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(theme = theme)
            }
            applyTheme()
        }
    }

    private fun applyTheme() {
        viewModelScope.launch {
            val isDynamic = state.value.isDynamicTheme
            val theme = state.value.theme.toPreferencesTheme(isDynamic)
            preferences.setTheme(theme)
        }
    }
}
