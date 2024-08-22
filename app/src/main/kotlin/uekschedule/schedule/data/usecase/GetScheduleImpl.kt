package uekschedule.schedule.data.usecase

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.collections.immutable.adapters.ImmutableListAdapter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import uekschedule.browser.domain.model.Schedulable
import uekschedule.schedule.data.dto.ClassDto
import uekschedule.schedule.data.dto.ScheduleDto
import uekschedule.schedule.domain.model.Class
import uekschedule.schedule.domain.model.Schedule
import uekschedule.schedule.domain.usecase.GetSchedule

class GetScheduleImpl(
    private val http: HttpClient,
) : GetSchedule {
    override suspend operator fun invoke(id: Schedulable.Id): Schedule {
        val schedule = http.get(buildUrl(id))
            .body<ScheduleDto>()
        return schedule.classes
            .map {
                Class(
                    name = it.name,
                    type = it.type,
                    timeframe = it.timeframe(),
                )
            }
            .let(::ImmutableListAdapter)
    }

    private fun buildUrl(id: Schedulable.Id): String {
        val type = when (id) {
            is Schedulable.Id.Group -> "G"
            is Schedulable.Id.Teacher -> "N"
        }
        return "https://planzajec.uek.krakow.pl/index.php?typ=$type&id=${id.value}&okres=2&xml"
    }

    private fun ClassDto.timeframe(): ClosedRange<Instant> {
        val date = LocalDate.parse(date)
        val startTime = LocalTime.parse(startTime)
        val endTime = LocalTime.parse(endTime.take(5))
        val zone = TimeZone.of("Europe/Warsaw")
        val start = date.atTime(startTime).toInstant(zone)
        val end = date.atTime(endTime).toInstant(zone)
        return start..end
    }
}
