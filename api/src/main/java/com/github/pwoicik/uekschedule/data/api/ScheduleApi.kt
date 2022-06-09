package com.github.pwoicik.uekschedule.data.api

import com.github.pwoicik.uekschedule.data.api.dto.GroupsDto
import com.github.pwoicik.uekschedule.data.api.dto.ScheduleDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleApi {

    @GET("index.php?typ=G&xml")
    suspend fun getGroups(): GroupsDto

    @GET("index.php?typ=G&okres=2&xml")
    suspend fun getSchedule(@Query("id") id: Long): ScheduleDto
}
