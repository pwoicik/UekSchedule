package com.github.pwoicik.uekschedule.feature_schedule.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import com.github.pwoicik.uekschedule.common.theme.UEKScheduleTheme
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            UEKScheduleTheme {
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
                        Timber.tag("root navGraph destination")
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
