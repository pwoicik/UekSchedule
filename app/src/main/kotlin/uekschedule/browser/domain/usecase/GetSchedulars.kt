package uekschedule.browser.domain.usecase

import arrow.core.Either
import kotlinx.collections.immutable.ImmutableList
import uekschedule.browser.domain.model.Schedular

interface GetSchedulars {
    suspend operator fun invoke(type: Schedular.Type): Either<Error, ImmutableList<Schedular>>

    enum class Error {
        Unsuccessful,
    }
}
