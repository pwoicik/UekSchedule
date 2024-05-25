package uekschedule.browser.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Schedular(
    val id: Id,
    val name: String,
) {
    sealed interface Id : Parcelable {
        val value: String

        @Parcelize
        @JvmInline
        value class Group(override val value: String) : Id

        @Parcelize
        @JvmInline
        value class Teacher(override val value: String) : Id
    }

    enum class Type {
        Group,
        Teacher,
    }
}
