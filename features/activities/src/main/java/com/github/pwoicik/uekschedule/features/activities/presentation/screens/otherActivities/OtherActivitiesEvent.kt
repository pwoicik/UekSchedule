package com.github.pwoicik.uekschedule.features.activities.presentation.screens.otherActivities

import com.github.pwoicik.uekschedule.domain.model.Activity

internal sealed class OtherActivitiesEvent {

    data class DeleteActivity(val activity: Activity) : OtherActivitiesEvent()
    data class UndoActivityDeletion(val activity: Activity) : OtherActivitiesEvent()

    object UserMessageSeen : OtherActivitiesEvent()
}
