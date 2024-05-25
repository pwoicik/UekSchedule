package uekschedule.browser.data.usecase

import io.ktor.client.HttpClient
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.adapters.ImmutableListAdapter
import uekschedule.browser.data.dto.GroupsDto
import uekschedule.browser.domain.model.Schedular
import uekschedule.browser.domain.usecase.GetSchedulars
import uekschedule.data.getCacheable

class GetSchedularsImpl(
    private val http: HttpClient,
) : GetSchedulars {
    override suspend fun invoke(type: Schedular.Type): ImmutableList<Schedular> =
        http.getCacheable<GroupsDto>(buildUrl(type))
            .groups
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

    private fun buildUrl(type: Schedular.Type): String =
        when (type) {
            Schedular.Type.Group -> "G"
            Schedular.Type.Teacher -> "N"
        }.let { "https://planzajec.uek.krakow.pl/index.php?typ=$it&xml" }
}
