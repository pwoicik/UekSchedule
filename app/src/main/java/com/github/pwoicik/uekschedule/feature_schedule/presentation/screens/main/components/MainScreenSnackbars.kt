package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarVisualsWithLoading
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SnackbarWithLoading

@Composable
fun MainScreenSnackbarWithLoading(snackbarData: SnackbarData) {
    SnackbarWithLoading(
        snackbarData = snackbarData,
        padding = 0.dp,
        shape = RectangleShape
    )
}

@Composable
fun MainScreenDismissibleActionSnackbar(
    snackbarData: SnackbarData,
    containerColor: Color = MaterialTheme.colorScheme.inverseSurface,
    contentColor: Color = MaterialTheme.colorScheme.inverseOnSurface
) {
    Snackbar(
        shape = RectangleShape,
        containerColor = containerColor,
        contentColor = contentColor,
        action = {
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = contentColor),
                onClick = { snackbarData.performAction() },
                content = { Text(snackbarData.visuals.actionLabel!!) }
            )
        },
        dismissAction = {
            IconButton(onClick = { snackbarData.dismiss() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = contentColor
                )
            }
        }
    ) {
        Text(snackbarData.visuals.message)
    }
}

class SnackbarVisualsWithPending(
    message: String,
) : SnackbarVisualsWithLoading(message = message)
