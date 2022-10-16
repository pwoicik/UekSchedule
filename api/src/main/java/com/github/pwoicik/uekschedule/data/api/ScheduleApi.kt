package com.github.pwoicik.uekschedule.data.api

import com.github.pwoicik.uekschedule.data.api.dto.SchedulablesDto
import com.github.pwoicik.uekschedule.data.api.dto.ScheduleDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleApi {

    @GET("index.php?typ=G&xml")
    suspend fun getGroups(): SchedulablesDto

    @GET("index.php?typ=G&okres=2&xml")
    suspend fun getGroupSchedule(@Query("id") id: Long): ScheduleDto

    @GET("index.php?typ=N&xml")
    suspend fun getTeachers(): SchedulablesDto

    @GET("index.php?typ=N&okres=2&xml")
    suspend fun getTeacherSchedule(@Query("id") id: Long): ScheduleDto
}
