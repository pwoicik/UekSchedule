package com.github.pwoicik.uekschedule.feature_schedule.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.github.pwoicik.uekschedule.common.theme.UEKScheduleTheme
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.NavGraphs
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            UEKScheduleTheme {
                val uiController = rememberSystemUiController()

                val isDarkMode = isSystemInDarkTheme()
                uiController.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = !isDarkMode
                )

                ProvideWindowInsets {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        modifier = Modifier.navigationBarsWithImePadding()
                    )
                }
            }
        }
    }
}
