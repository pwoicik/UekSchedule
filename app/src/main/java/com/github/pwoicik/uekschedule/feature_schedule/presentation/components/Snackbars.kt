package com.github.pwoicik.uekschedule.feature_schedule.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SnackbarHost(
    hostState: SnackbarHostState
) {
    SnackbarHost(hostState = hostState) { snackbarData ->
        when (snackbarData.visuals) {
            is SnackbarVisualsWithError ->
                SnackbarWithError(snackbarData = snackbarData)
            is SnackbarVisualsWithSuccess ->
                SnackbarWithSuccess(snackbarData = snackbarData)
            is SnackbarVisualsWithLoading ->
                SnackbarWithLoading(snackbarData = snackbarData)
        }
    }

}

@Composable
fun SnackbarWithError(
    snackbarData: SnackbarData
) {
    Snackbar(
        snackbarData = snackbarData,
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        actionColor = MaterialTheme.colorScheme.error
    )
}

@Composable
fun SnackbarWithSuccess(
    snackbarData: SnackbarData
) {
    Snackbar(snackbarData)
}

@Composable
fun SnackbarWithLoading(
    snackbarData: SnackbarData
) {
    Snackbar(modifier = Modifier.padding(12.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(snackbarData.visuals.message)
            CircularProgressIndicator(
                strokeWidth = 3.dp,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

data class SnackbarVisualsWithError(
    override val message: String,
    override val actionLabel: String?
) : SnackbarVisuals {
    override val duration: SnackbarDuration
        get() = SnackbarDuration.Indefinite
    override val withDismissAction: Boolean
        get() = false
}

data class SnackbarVisualsWithSuccess(
    override val message: String
) : SnackbarVisuals {
    override val duration: SnackbarDuration
        get() = SnackbarDuration.Short
    override val actionLabel: String?
        get() = null
    override val withDismissAction: Boolean
        get() = false
}

data class SnackbarVisualsWithLoading(
    override val message: String
) : SnackbarVisuals {
    override val actionLabel: String?
        get() = null
    override val duration: SnackbarDuration
        get() = SnackbarDuration.Indefinite
    override val withDismissAction: Boolean
        get() = false
}
