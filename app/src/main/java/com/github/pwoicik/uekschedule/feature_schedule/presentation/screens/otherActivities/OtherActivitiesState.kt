package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.otherActivities

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity
import javax.annotation.concurrent.Immutable

@Immutable
data class OtherActivitiesState(

    val activities: List<Activity>? = null,
    val userMessage: UserMessage = UserMessage.None
)

sealed class UserMessage {

    object None : UserMessage()
    data class ActivityDeleted(val activity: Activity) : UserMessage()
}
