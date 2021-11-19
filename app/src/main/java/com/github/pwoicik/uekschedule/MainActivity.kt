package com.github.pwoicik.uekschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.pwoicik.uekschedule.screen.scheduleScreen.ScheduleScreen
import com.github.pwoicik.uekschedule.ui.theme.UEKScheduleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UekApp()
        }
    }
}

@Composable
fun UekApp() {
    UEKScheduleTheme {
        Scaffold { innerPadding ->
            val navController = rememberNavController()

            UekNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UekNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "main",
        modifier = modifier
    ) {
        composable("main") {
            ScheduleScreen()
        }
    }
}