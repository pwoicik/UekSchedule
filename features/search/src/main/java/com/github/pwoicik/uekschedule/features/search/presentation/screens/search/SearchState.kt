package com.github.pwoicik.uekschedule.features.search.presentation.screens.search

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.common.R
import com.github.pwoicik.uekschedule.domain.model.Schedulable
import com.github.pwoicik.uekschedule.domain.model.SchedulableType

internal enum class SearchPages(@StringRes val title: Int) {
    Groups(R.string.groups),
    Teachers(R.string.teachers)
    ;

    fun toSchedulableType() = when (this) {
        Groups -> SchedulableType.Group
        Teachers -> SchedulableType.Teacher
    }
}

@Immutable
internal data class SearchState(

    val userMessage: UserMessage? = null,
    val currentPage: SearchPages = SearchPages.Groups,
    val searchableSchedulablesState: SearchableSchedulablesState = SearchableSchedulablesState()
) {

    val currentSchedulableType: SchedulableType
        inline get() = currentPage.toSchedulableType()
}

@Immutable
internal data class SearchableSchedulablesState(

    val searchValue: TextFieldValue = TextFieldValue(),
    val isLoading: Boolean = true,
    val didTry: Boolean = false,
    val isSaving: Boolean = false,
    val items: List<Schedulable>? = null,
    val filteredItems: List<Schedulable> = emptyList()
)

internal sealed interface UserMessage {

    data class Error(val eventToRepeat: SearchEvent) : UserMessage
    data class SavingSchedulable(val schedulable: Schedulable) : UserMessage
    data class SavedSchedulable(val schedulable: Schedulable) : UserMessage
}
