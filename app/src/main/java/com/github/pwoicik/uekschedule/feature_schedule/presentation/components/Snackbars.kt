package com.github.pwoicik.uekschedule.feature_schedule.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState = hostState) { snackbarData ->
        when (snackbarData.visuals) {
            is SnackbarVisualsWithError -> SnackbarWithError(snackbarData)
            is SnackbarVisualsWithLoading -> SnackbarWithLoading(snackbarData)
            is SnackbarVisualsWithSuccess -> SnackbarWithSuccess(snackbarData)
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
    snackbarData: SnackbarData,
    padding: Dp = 12.dp,
    shape: Shape = RoundedCornerShape(4.dp)
) {
    Snackbar(
        shape = shape,
        modifier = Modifier.padding(padding)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(snackbarData.visuals.message)

            val visuals = snackbarData.visuals as SnackbarVisualsWithLoading
            if (visuals.progress == null) {
                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                val progress by visuals.progress.collectAsState()
                CircularProgressIndicator(
                    progress = progress.coerceAtLeast(0.1f),
                    strokeWidth = 3.dp,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

class SnackbarVisualsWithError(
    override val message: String,
    override val actionLabel: String?,
    override val duration: SnackbarDuration = SnackbarDuration.Indefinite,
    override val withDismissAction: Boolean = false
) : SnackbarVisuals

class SnackbarVisualsWithSuccess(
    override val message: String,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false
) : SnackbarVisuals

open class SnackbarVisualsWithLoading(
    override val message: String,
    override val duration: SnackbarDuration = SnackbarDuration.Indefinite,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    val progress: StateFlow<Float>? = null
) : SnackbarVisuals
