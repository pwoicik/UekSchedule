package uekschedule.browser.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

data class Schedular(
    val id: Id,
    val name: String,
) {
    @Immutable
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
