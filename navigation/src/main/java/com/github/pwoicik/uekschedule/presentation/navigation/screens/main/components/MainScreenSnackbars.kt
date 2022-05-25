package com.github.pwoicik.uekschedule.presentation.navigation.screens.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.presentation.components.SnackbarVisualsWithError
import com.github.pwoicik.uekschedule.presentation.components.SnackbarVisualsWithLoading
import com.github.pwoicik.uekschedule.presentation.components.SnackbarWithLoading

@Composable
internal fun MainScreenSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState) {
        when(it.visuals) {
            is SnackbarVisualsWithPending -> MainScreenSnackbarWithLoading(it)
            is SnackbarVisualsWithSuccess -> MainScreenDismissibleActionSnackbar(it)
            is SnackbarVisualsWithLoading -> MainScreenSnackbarWithLoading(it)
            is SnackbarVisualsWithError -> MainScreenDismissibleActionSnackbar(
                snackbarData = it,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
internal fun MainScreenSnackbarWithLoading(snackbarData: SnackbarData) {
    SnackbarWithLoading(
        snackbarData = snackbarData,
        padding = 0.dp,
        shape = RectangleShape
    )
}

@Composable
internal fun MainScreenDismissibleActionSnackbar(
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

internal class SnackbarVisualsWithSuccess(
    message: String,
    override val actionLabel: String? = null
) : com.github.pwoicik.uekschedule.presentation.components.SnackbarVisualsWithSuccess(message) {
    override val duration: SnackbarDuration = SnackbarDuration.Indefinite
}

internal class SnackbarVisualsWithPending(
    override val message: String,
) : SnackbarVisualsWithLoading(message)
