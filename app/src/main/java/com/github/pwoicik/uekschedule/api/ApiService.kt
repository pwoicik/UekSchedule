package com.github.pwoicik.uekschedule.api

import com.github.pwoicik.uekschedule.model.Schedule
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("index.php?typ=G&okres=1&xml")
    suspend fun getSchedule(@Query("id") id: String): Schedule

    companion object {
        private var apiService: ApiService? = null
        fun getInstance(): ApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl("https://planzajec.uek.krakow.pl/")
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}
