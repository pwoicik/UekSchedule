package com.github.pwoicik.uekschedule.features.search.presentation.screens.search

import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.domain.model.Schedulable

internal sealed interface SearchEvent {

    data class SearchTextChanged(val newValue: TextFieldValue) : SearchEvent
    data class PageChanged(val targetScreen: SearchPages) : SearchEvent
    data class SchedulableSaveButtonClicked(val schedulable: Schedulable) : SearchEvent
    object RetrySchedulablesFetch : SearchEvent

    object UserMessageSeen : SearchEvent
}
