package com.github.pwoicik.uekschedule.features.groups.presentation.screens.savedGroups

import androidx.compose.runtime.Immutable
import com.github.pwoicik.uekschedule.domain.model.Group
import com.github.pwoicik.uekschedule.domain.model.GroupWithClasses

@Immutable
internal data class SavedGroupsState(

    val groups: List<Group>? = null,
    val userMessage: UserMessage = UserMessage.None
)

internal sealed class UserMessage {

    object None : UserMessage()
    data class GroupDeleted(val gwc: GroupWithClasses) : UserMessage()
}
