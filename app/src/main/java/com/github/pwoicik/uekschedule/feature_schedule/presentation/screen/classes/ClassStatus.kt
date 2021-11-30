package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.classes

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Class
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

//enum class ClassStatus {
//    NOT_STARTED,
//    IN_PROGRESS,
//    ENDED;
//
//    companion object {
//        fun fromClass(clazz: Class, timeNow: ZonedDateTime): ClassStatus {
//            return when {
//                timeNow.isBefore(clazz.startDateTime) -> NOT_STARTED
//                timeNow.isBefore(clazz.endDateTime) -> IN_PROGRESS
//                else -> ENDED
//            }
//        }
//    }
//}

sealed class ClassStatus {
    data class NotStarted(val minutesToStart: Long) : ClassStatus()
    data class InProgress(val minutesRemaining: Long) : ClassStatus()
    object Ended : ClassStatus()
}

fun Class.status(timeNow: ZonedDateTime): ClassStatus {
    return when {
        timeNow.isBefore(startDateTime) -> ClassStatus.NotStarted(
            timeNow.until(startDateTime, ChronoUnit.MINUTES)
        )
        timeNow.isBefore(endDateTime) -> ClassStatus.InProgress(
            timeNow.until(endDateTime, ChronoUnit.MINUTES)
        )
        else -> ClassStatus.Ended
    }
}
