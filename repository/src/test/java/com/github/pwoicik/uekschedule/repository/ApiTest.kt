package com.github.pwoicik.uekschedule.repository

import com.github.pwoicik.uekschedule.common.Constants
import com.github.pwoicik.uekschedule.common.domain.ScheduleRepository
import com.github.pwoicik.uekschedule.data.api.ScheduleApi
import com.github.pwoicik.uekschedule.data.db.ScheduleDatabase
import com.github.pwoicik.uekschedule.domain.model.Schedulable
import com.github.pwoicik.uekschedule.domain.model.SchedulableType
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.tls.HandshakeCertificates
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.kotlin.mock
import retrofit2.HttpException
import retrofit2.Retrofit
import javax.net.ssl.SSLProtocolException

class ApiTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun groupDtos_mapToModelsCorrectly(): Unit = runBlocking {
        val schedulables = produce {
            getAllSchedulables().forEach { send(it) }
        }
        repeat(10) {
            launch {
                for (s in schedulables) {
                    testSchedulable(s)
                }
            }
        }
        // Test passes if there are no uncaught exceptions
    }

    private suspend fun getAllSchedulables() =
        repo.getAllSchedulables(SchedulableType.Group).getOrThrow() +
                repo.getAllSchedulables(SchedulableType.Teacher).getOrThrow()

    private suspend fun testSchedulable(s: Schedulable) {
        while (true) {
            repo.fetchSchedule(s.id, s.type)
                .onFailure {
                    when (it) {
                        is HttpException, is SSLProtocolException -> {
                            println("failure, retrying (${s.name})")
                        }

                        else -> {
                            throw Exception("fetching failed for group ${s.name}", it)
                        }
                    }
                }
                .exceptionOrNull()
                ?: break
        }
    }

    companion object {
        lateinit var repo: ScheduleRepository

        @BeforeClass
        @JvmStatic
        fun setup() {
            val client = OkHttpClient.Builder().run {
                val certificates = HandshakeCertificates.Builder()
                    .addPlatformTrustedCertificates()
                    .addInsecureHost("planzajec.uek.krakow.pl")
                    .build()

                sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager)
            }.build()

            val api = Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(
                    TikXmlConverterFactory.create(
                        TikXml.Builder().exceptionOnUnreadXml(false).build()
                    )
                )
                .build()
                .create(ScheduleApi::class.java)
            val db = mock<ScheduleDatabase>()

            repo = ScheduleRepositoryImpl(api, db)
        }
    }
}
