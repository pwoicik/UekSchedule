@file:Suppress("PackageDirectoryMismatch")

package com.github.pwoicik.uekschedule.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.lyricist.ProvideStrings
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuitx.android.rememberAndroidScreenAwareNavigator
import com.slack.circuitx.gesturenavigation.GestureNavigationDecoration
import org.koin.android.ext.android.inject
import uekschedule.home.ui.HomeScreen
import uekschedule.ui.theme.UekScheduleTheme

class MainActivity : ComponentActivity() {
    private val circuit: Circuit by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UekScheduleTheme {
                ProvideStrings {
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Circuit()
                    }
                }
            }
        }
    }

    @Composable
    private fun Circuit() {
        val backstack = rememberSaveableBackStack(HomeScreen)
        val circuitNavigator = rememberCircuitNavigator(backstack)
        val navigator = rememberAndroidScreenAwareNavigator(circuitNavigator, this)
        CircuitCompositionLocals(circuit = circuit) {
            ContentWithOverlays {
                NavigableCircuitContent(
                    navigator = navigator,
                    backStack = backstack,
                    decoration = GestureNavigationDecoration(
                        onBackInvoked = navigator::pop,
                    ),
                )
            }
        }
    }
}
