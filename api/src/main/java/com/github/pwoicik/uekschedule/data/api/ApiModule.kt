package com.github.pwoicik.uekschedule.data.api

import com.github.pwoicik.uekschedule.common.Constants
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object ApiModule {

    @Provides
    @Singleton
    fun provideApi() : ScheduleApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(
                TikXmlConverterFactory.create(
                    TikXml.Builder()
                        .exceptionOnUnreadXml(false)
                        .build()
                )
            )
            .build()
            .create(ScheduleApi::class.java)
    }
}
