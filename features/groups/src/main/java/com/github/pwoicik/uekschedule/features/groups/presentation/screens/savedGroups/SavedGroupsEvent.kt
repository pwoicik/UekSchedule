package com.github.pwoicik.uekschedule.features.groups.presentation.screens.savedGroups

import com.github.pwoicik.uekschedule.domain.model.Schedulable
import com.github.pwoicik.uekschedule.domain.model.SchedulableWithClasses

internal sealed class SavedGroupsEvent {

    data class FavoriteGroup(val group: Schedulable) : SavedGroupsEvent()
    data class DeleteGroup(val group: Schedulable) : SavedGroupsEvent()
    data class UndoGroupDeletion(val gwc: SchedulableWithClasses) : SavedGroupsEvent()

    object UserMessageSeen : SavedGroupsEvent()
}
