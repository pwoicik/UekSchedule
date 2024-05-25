@file:Suppress("PackageDirectoryMismatch")

package com.github.pwoicik.uekschedule

import android.app.Application
import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import uekschedule.di.AppModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.setLogWriters(platformLogWriter(DefaultFormatter))
        Logger.setMinSeverity(
            if (BuildConfig.DEBUG) {
                Severity.Debug
            } else {
                Severity.Error
            },
        )
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger(Level.DEBUG)
            }
            androidContext(this@App)
            modules(AppModule)
        }
    }
}
