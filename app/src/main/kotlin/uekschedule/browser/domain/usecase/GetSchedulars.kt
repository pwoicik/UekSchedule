package uekschedule.browser.domain.usecase

import kotlinx.collections.immutable.ImmutableList
import uekschedule.browser.domain.model.Schedular

interface GetSchedulars {
    suspend operator fun invoke(type: Schedular.Type): ImmutableList<Schedular>
}
