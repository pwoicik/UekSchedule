package com.github.pwoicik.uekschedule.features.groups.presentation.screens.savedGroups

import androidx.compose.runtime.Immutable
import com.github.pwoicik.uekschedule.domain.model.Schedulable
import com.github.pwoicik.uekschedule.domain.model.SchedulableWithClasses

@Immutable
internal data class SavedGroupsState(

    val groups: List<Schedulable>? = null,
    val userMessage: UserMessage = UserMessage.None
)

internal sealed class UserMessage {

    data object None : UserMessage()
    data class GroupDeleted(val gwc: SchedulableWithClasses) : UserMessage()
}
