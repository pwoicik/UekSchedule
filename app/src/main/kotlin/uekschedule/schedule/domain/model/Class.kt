package uekschedule.schedule.domain.model

import kotlinx.datetime.Instant

data class Class(
    val name: String,
    val type: String?,
    val timeframe: ClosedRange<Instant>,
)
