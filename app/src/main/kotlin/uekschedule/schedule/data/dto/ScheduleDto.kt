package uekschedule.schedule.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@SerialName("plan-zajec")
data class ScheduleDto(
    @SerialName("id")
    val id: String,
    @SerialName("nazwa")
    val name: String,
    val classes: List<ClassDto>,
)

@Serializable
@SerialName("zajecia")
data class ClassDto(
    @XmlElement
    @SerialName("termin")
    val date: String,
    @XmlElement
    @SerialName("od-godz")
    val startTime: String,
    @XmlElement
    @SerialName("do-godz")
    val endTime: String,
    @XmlElement
    @SerialName("przedmiot")
    val name: String,
    @XmlElement
    @SerialName("typ")
    val type: String,
    val teachers: List<TeacherDto>,
    @XmlElement
    @SerialName("sala")
    val room: String?,
    @XmlElement
    @SerialName("uwagi")
    val note: String?
)

@Serializable
@SerialName("nauczyciel")
data class TeacherDto(
    @XmlValue
    val name: String,
    @SerialName("moodle")
    val id: String?,
)
