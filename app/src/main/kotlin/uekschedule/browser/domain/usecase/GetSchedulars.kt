package uekschedule.browser.domain.usecase

import arrow.core.Either
import kotlinx.collections.immutable.ImmutableList
import uekschedule.browser.domain.model.Schedulable

interface GetSchedulables {
    suspend operator fun invoke(type: Schedulable.Type): Either<Error, ImmutableList<Schedulable>>

    enum class Error {
        Unsuccessful,
    }
}
