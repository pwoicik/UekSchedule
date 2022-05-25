package com.github.pwoicik.uekschedule.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.github.pwoicik.uekschedule.BuildConfig
import com.github.pwoicik.uekschedule.common.domain.PreferencesManager
import com.github.pwoicik.uekschedule.domain.model.Preferences
import com.github.pwoicik.uekschedule.presentation.navigation.RootNavHost
import com.github.pwoicik.uekschedule.presentation.util.requestReview
import com.github.pwoicik.uekschedule.presentation.theme.UEKScheduleTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch {
            delay(2.seconds)
            val currentAppVersion = BuildConfig.VERSION_CODE
            val lastUsedAppVersion = preferences.lastUsedAppVersion.first()
            if (lastUsedAppVersion == currentAppVersion) {
                requestReview(this@MainActivity)
            } else {
                preferences.setLastUsedAppVersion(currentAppVersion)
            }
        }

        setContent {
            val preferredTheme by preferences.theme.collectAsState(Preferences.Defaults.THEME)
            UEKScheduleTheme(preferredTheme) {
                val uiController = rememberSystemUiController()
                val backgroundColor = MaterialTheme.colorScheme.background
                LaunchedEffect(preferredTheme) {
                    uiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = backgroundColor.luminance() >= 0.5,
                        isNavigationBarContrastEnforced = false
                    )
                }

                RootNavHost()
            }
        }
    }
}
