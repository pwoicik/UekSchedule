package uekschedule.browser.data.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import io.ktor.client.HttpClient
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.adapters.ImmutableListAdapter
import logcat.asLog
import logcat.logcat
import uekschedule.browser.data.dto.GroupsDto
import uekschedule.browser.domain.model.Schedular
import uekschedule.browser.domain.usecase.GetSchedulars
import uekschedule.data.getCacheable
import javax.net.ssl.SSLProtocolException

class GetSchedularsImpl(
    private val http: HttpClient,
) : GetSchedulars {
    override suspend fun invoke(
        type: Schedular.Type,
    ): Either<GetSchedulars.Error, ImmutableList<Schedular>> = either {
        val url = buildUrl(type)
        suspend fun get() = http.getCacheable<GroupsDto>(url)
        val res = get()
            .fold(
                ifLeft = {
                    if (it is SSLProtocolException) {
                        get()
                    } else {
                        it.left()
                    }
                },
                ifRight = { it.right() },
            )
            .mapLeft {
                logcat { it.asLog() }
                GetSchedulars.Error.Unsuccessful
            }
            .bind()
        return res.groups
            .map {
                Schedular(
                    id = when (it.type) {
                        "G" -> Schedular.Id.Group(it.id)
                        "N" -> Schedular.Id.Teacher(it.id)
                        else -> throw NotImplementedError()
                    },
                    name = it.name,
                )
            }
            .let(::ImmutableListAdapter)
            .right()
    }

    private fun buildUrl(type: Schedular.Type): String =
        when (type) {
            Schedular.Type.Group -> "G"
            Schedular.Type.Teacher -> "N"
        }.let { "https://planzajec.uek.krakow.pl/index.php?typ=$it&xml" }
}
