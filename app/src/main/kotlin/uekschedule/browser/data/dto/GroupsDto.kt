package uekschedule.browser.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("plan-zajec")
data class GroupsDto(
    val groups: List<GroupDto>,
)

@Serializable
@SerialName("zasob")
data class GroupDto(
    @SerialName("id")
    val id: String,
    @SerialName("typ")
    val type: String,
    @SerialName("nazwa")
    val name: String,
)
