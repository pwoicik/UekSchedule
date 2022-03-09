package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.savedGroups

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.GroupWithClasses

sealed class SavedGroupsEvent {

    data class DeleteActivityButtonClicked(val activity: Activity) : SavedGroupsEvent()
    data class DeleteGroupButtonClicked(val group: Group) : SavedGroupsEvent()
    data class UndoActivityDeletion(val activity: Activity) : SavedGroupsEvent()
    data class UndoGroupDeletion(val gwc: GroupWithClasses) : SavedGroupsEvent()
}
