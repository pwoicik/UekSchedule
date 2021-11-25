package com.github.pwoicik.uekschedule.api

import com.github.pwoicik.uekschedule.database.Group
import com.github.pwoicik.uekschedule.database.GroupWithClasses

class ApiRepository {

    private val apiService = ApiService.getInstance()

    suspend fun getGroups(): List<Group> = apiService.getGroups().groups!!.map {
        Group(it.id, it.name)
    }

    suspend fun getSchedule(groupId: Long): GroupWithClasses {
        val s = apiService.getSchedule(groupId)
        return GroupWithClasses.fromModelSchedule(s)
    }
}
