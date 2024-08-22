package com.github.pwoicik.uekschedule.presentation

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import com.github.pwoicik.uekschedule.BuildConfig
import com.github.pwoicik.uekschedule.common.domain.PreferencesManager
import com.github.pwoicik.uekschedule.domain.model.Preferences
import com.github.pwoicik.uekschedule.presentation.navigation.RootNavHost
import com.github.pwoicik.uekschedule.presentation.theme.UEKScheduleTheme
import com.github.pwoicik.uekschedule.presentation.util.requestReview
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val preferredTheme by preferences.theme.collectAsState(Preferences.Defaults.THEME)
            UEKScheduleTheme(preferredTheme) {
                val backgroundColor = MaterialTheme.colorScheme.background
                LaunchedEffect(preferredTheme) {
                    this@MainActivity.enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.auto(
                            lightScrim = Color.Transparent.toArgb(),
                            darkScrim = Color.Transparent.toArgb(),
                            detectDarkMode = { backgroundColor.luminance() < 0.5 },
                        ),
                    )
                }

                RootNavHost()
            }

            LaunchedEffect(Unit) {
                delay(2.seconds)
                val currentAppVersion = BuildConfig.VERSION_CODE
                val lastUsedAppVersion = preferences.lastUsedAppVersion.first()
                if (lastUsedAppVersion == currentAppVersion) {
                    requestReview(this@MainActivity)
                } else {
                    preferences.setLastUsedAppVersion(currentAppVersion)
                }
            }
        }
    }
}
