package com.github.pwoicik.uekschedule

import android.util.Log
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import javax.inject.Inject
import javax.net.ssl.SSLProtocolException

/**
 * I don't know how to write tests yet so it's probably not very good
 */
@HiltAndroidTest
class ApiTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: ScheduleRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun classDto_isCorrect(): Unit = runBlocking {
        val groups = repo.getAllGroups()
        val tasks = groups.map { group ->
            Log.d("fetching group", group.name)
            async {
                var error: Boolean
                do {
                    error = try {
                        repo.fetchSchedule(group.id)
                        Log.d("fetching finished", group.name)
                        false
                    } catch (e: Exception) {
                        when (e) {
                            is HttpException, is SSLProtocolException -> {
                                Log.d("fetching failed, retrying", group.name)
                                true
                            }
                            else -> {
                                throw Exception("fetching failed for group ${group.name}", e)
                            }
                        }
                    }
                } while (error)
            }
        }
        tasks.awaitAll()
    }
}
