package com.github.pwoicik.uekschedule

import com.github.pwoicik.uekschedule.feature_schedule.common.Constants
import com.github.pwoicik.uekschedule.feature_schedule.data.api.ScheduleApi
import com.github.pwoicik.uekschedule.feature_schedule.data.db.ScheduleDatabase
import com.github.pwoicik.uekschedule.feature_schedule.data.repository.ScheduleRepositoryImpl
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

    @Test
    fun groupDtos_mapToModelsCorrectly(): Unit = runBlocking {
        val groups = repo.getAllGroups().getOrThrow()
        val tasks = groups.map { group ->
            println("fetching group ${group.name}")
            async {
                while (true) {
                    repo.fetchSchedule(group.id)
                        .onSuccess {
                            println("success (${group.name})")
                        }
                        .onFailure {
                            when (it) {
                                is HttpException, is SSLProtocolException -> {
                                    println("failure, retrying (${group.name})")
                                }
                                else -> {
                                    throw Exception("fetching failed for group ${group.name}", it)
                                }
                            }
                        }
                        .exceptionOrNull()
                        ?: break
                }
            }
        }
        tasks.awaitAll()
        // Test passes if there are no uncaught exceptions
    }
}
