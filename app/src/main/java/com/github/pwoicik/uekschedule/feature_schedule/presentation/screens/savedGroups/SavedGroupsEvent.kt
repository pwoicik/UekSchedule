package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.savedGroups

import com.github.pwoicik.uekschedule.domain.model.Group
import com.github.pwoicik.uekschedule.domain.model.GroupWithClasses

sealed class SavedGroupsEvent {

    data class FavoriteGroup(val group: Group) : SavedGroupsEvent()
    data class DeleteGroup(val group: Group) : SavedGroupsEvent()
    data class UndoGroupDeletion(val gwc: GroupWithClasses) : SavedGroupsEvent()

    object UserMessageSeen : SavedGroupsEvent()
}
