@file:Suppress("PackageDirectoryMismatch")

package com.github.pwoicik.uekschedule

import android.app.Application
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import uekschedule.di.AppModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
        }
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger(Level.DEBUG)
            }
            androidContext(this@App)
            modules(AppModule)
        }
    }
}
