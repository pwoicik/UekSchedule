package com.github.pwoicik.uekschedule.feature_schedule.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import com.github.pwoicik.uekschedule.common.theme.UEKScheduleTheme
import com.github.pwoicik.uekschedule.feature_schedule.data.preferences.PreferencesManager
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Preferences
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.NavGraphs
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.navDestination
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: PreferencesManager

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val preferredTheme by preferences.theme.collectAsState(Preferences.Defaults.THEME)
            UEKScheduleTheme(preferredTheme) {
                ProvideWindowInsets {
                    val uiController = rememberSystemUiController()

                    val isDarkMode = isSystemInDarkTheme()
                    uiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !isDarkMode
                    )

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
