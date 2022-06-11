package com.github.pwoicik.uekschedule.data.db

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        ScheduleDatabase::class.java
    )

    @Test
    fun migrate2to3() {
        helper.createDatabase(TEST_DB, 2).apply {
            execSQL(
                """
                insert into groups values(
                    1,
                    "group",
                    true
                )
            """.trimIndent()
            )

            execSQL(
                """
                insert into classes values(
                    1,
                    "subject",
                    1,
                    1,
                    "type",
                    "details",
                    "teachers",
                    "location"
                )
            """.trimIndent()
            )

            close()
        }

        val db = helper.runMigrationsAndValidate(TEST_DB, 3, true)

        db.query("select * from schedulables").apply {
            assert(count == 1)
        }
    }

    companion object {
        private const val TEST_DB = "migration-test"
    }
}
