package com.github.pwoicik.uekschedule.api

import com.github.pwoicik.uekschedule.api.model.Group
import com.github.pwoicik.uekschedule.database.GroupWithClasses

class ApiRepository {

    private val apiService = ApiService.getInstance()

    suspend fun getGroups(): List<Group>? = apiService.getGroups().groups

    suspend fun getSchedule(groupId: Long): GroupWithClasses {
        val s = apiService.getSchedule(groupId)
        return GroupWithClasses.fromModelSchedule(s)
    }
}
