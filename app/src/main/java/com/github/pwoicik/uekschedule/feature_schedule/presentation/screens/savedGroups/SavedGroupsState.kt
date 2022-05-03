package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.savedGroups

import androidx.compose.runtime.Immutable
import com.github.pwoicik.uekschedule.domain.model.Group
import com.github.pwoicik.uekschedule.domain.model.GroupWithClasses

@Immutable
data class SavedGroupsState(

    val groups: List<Group>? = null,
    val userMessage: UserMessage = UserMessage.None
)

sealed class UserMessage {

    object None : UserMessage()
    data class GroupDeleted(val gwc: GroupWithClasses) : UserMessage()
}
