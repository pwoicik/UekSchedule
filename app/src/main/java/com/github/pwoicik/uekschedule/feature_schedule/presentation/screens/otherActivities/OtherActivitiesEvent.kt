package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.otherActivities

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity

sealed class OtherActivitiesEvent {

    data class DeleteActivity(val activity: Activity) : OtherActivitiesEvent()
    data class UndoActivityDeletion(val activity: Activity) : OtherActivitiesEvent()

    object UserMessageSeen : OtherActivitiesEvent()
}
