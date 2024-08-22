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
import uekschedule.browser.domain.model.Schedulable
import uekschedule.browser.domain.usecase.GetSchedulables
import uekschedule.data.getCacheable
import javax.net.ssl.SSLProtocolException

class GetSchedulablesImpl(
    private val http: HttpClient,
) : GetSchedulables {
    override suspend fun invoke(
        type: Schedulable.Type,
    ): Either<GetSchedulables.Error, ImmutableList<Schedulable>> = either {
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
                GetSchedulables.Error.Unsuccessful
            }
            .bind()
        return res.groups
            .map {
                Schedulable(
                    id = when (it.type) {
                        "G" -> Schedulable.Id.Group(it.id)
                        "N" -> Schedulable.Id.Teacher(it.id)
                        else -> throw NotImplementedError()
                    },
                    name = it.name,
                )
            }
            .let(::ImmutableListAdapter)
            .right()
    }

    private fun buildUrl(type: Schedulable.Type): String =
        when (type) {
            Schedulable.Type.Group -> "G"
            Schedulable.Type.Teacher -> "N"
        }.let { "https://planzajec.uek.krakow.pl/index.php?typ=$it&xml" }
}
