package com.github.pwoicik.uekschedule.feature_schedule.common

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

fun timeFlow() = flow {
    while (true) {
        val timeNow = LocalDateTime.now()
        emit(timeNow)
        delay((60 - timeNow.second) * 1000L)
    }
}
