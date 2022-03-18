package com.github.pwoicik.uekschedule.feature_schedule.presentation

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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.pwoicik.uekschedule.BuildConfig
import com.github.pwoicik.uekschedule.common.theme.UEKScheduleTheme
import com.github.pwoicik.uekschedule.feature_schedule.data.preferences.PreferencesManager
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Preferences
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.NavGraphs
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.navDestination
import com.github.pwoicik.uekschedule.feature_schedule.presentation.util.requestReview
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: PreferencesManager

    @OptIn(ExperimentalTime::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch {
            val currentAppVersion = BuildConfig.VERSION_CODE
            val lastUsedAppVersion = preferences.lastUsedAppVersion.first()
            if (lastUsedAppVersion != currentAppVersion) {
                delay(5.seconds)
                requestReview(this@MainActivity)
                preferences.setLastUsedAppVersion(currentAppVersion)
            }
        }

        setContent {
            val preferredTheme by preferences.theme.collectAsState(Preferences.Defaults.THEME)
            UEKScheduleTheme(preferredTheme) {
                ProvideWindowInsets {
                    val uiController = rememberSystemUiController()
                    val backgroundColor = MaterialTheme.colorScheme.background
                    LaunchedEffect(preferredTheme) {
                        uiController.setSystemBarsColor(
                            color = Color.Transparent,
                            darkIcons = backgroundColor.luminance() >= 0.5
                        )
                    }

                    val navController = rememberNavController()
                    val currentDestination = navController.currentBackStackEntryAsState()
                        .value?.navDestination
                    LaunchedEffect(currentDestination) {
                        Timber
                            .tag("root navGraph destination")
                            .d(currentDestination?.route.toString())
                    }
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = navController
                    )
                }
            }
        }
    }
}
