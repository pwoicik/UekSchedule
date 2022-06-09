package com.github.pwoicik.uekschedule.features.activities.presentation.screens.otherActivities

import com.github.pwoicik.uekschedule.domain.model.Activity
import javax.annotation.concurrent.Immutable

@Immutable
internal data class OtherActivitiesState(

    val activities: List<Activity>? = null,
    val userMessage: UserMessage = UserMessage.None
)

internal sealed class UserMessage {

    object None : UserMessage()
    data class ActivityDeleted(val activity: Activity) : UserMessage()
}
