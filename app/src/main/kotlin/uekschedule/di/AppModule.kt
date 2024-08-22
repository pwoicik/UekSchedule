package uekschedule.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.slack.circuit.foundation.Circuit
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.xml.xml
import nl.adaptivity.xmlutil.serialization.XML
import org.koin.dsl.module
import uekschedule.browser.di.BrowserModule
import uekschedule.data.db.Database
import uekschedule.home.di.HomeModule
import uekschedule.mygroups.di.MyGroupsModule
import uekschedule.schedule.di.ScheduleModule

val AppModule = module {
    includes(HomeModule, BrowserModule, MyGroupsModule, ScheduleModule)

    factory {
        Circuit.Builder()
            .addPresenterFactories(getAll())
            .addUiFactories(getAll())
            .build()
    }

    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                xml(
                    XML {
                        recommended {
                            ignoreUnknownChildren()
                        }
                    },
                )
            }
        }
    }

    single {
        Database(
            AndroidSqliteDriver(
                Database.Schema,
                get(),
                "uekschedule.db",
            ),
        )
    }
}
