package com.github.pwoicik.uekschedule.presentation.components

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
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
import com.github.pwoicik.uekschedule.resources.R
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState = hostState) { snackbarData ->
        when (snackbarData.visuals) {
            is SnackbarVisualsWithError -> SnackbarWithError(snackbarData)
            is SnackbarVisualsWithLoading -> SnackbarWithLoading(snackbarData)
            is SnackbarVisualsWithSuccess -> SnackbarWithSuccess(snackbarData)
            is SnackbarVisualsWithUndo -> SnackbarWithUndo(snackbarData)
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
) = Snackbar(snackbarData)

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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = snackbarData.visuals.message,
                modifier = Modifier.weight(1f)
            )

            val visuals = snackbarData.visuals as SnackbarVisualsWithLoading
            if (visuals.progress == null) {
                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                val progress by visuals.progress.collectAsState()
                val animatedProgress by animateFloatAsState(progress.coerceAtLeast(0.1f))
                CircularProgressIndicator(
                    progress = animatedProgress,
                    strokeWidth = 3.dp,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun SnackbarWithUndo(
    snackbarData: SnackbarData
) = Snackbar(snackbarData)

open class SnackbarVisualsWithError(
    override val message: String,
    override val actionLabel: String?,
    override val withDismissAction: Boolean = false
) : SnackbarVisuals {
    override val duration: SnackbarDuration = SnackbarDuration.Indefinite
}

open class SnackbarVisualsWithSuccess(
    override val message: String
) : SnackbarVisuals {
    override val duration: SnackbarDuration = SnackbarDuration.Short
    override val actionLabel: String? = null
    override val withDismissAction: Boolean = false
}

open class SnackbarVisualsWithLoading(
    override val message: String,
    val progress: StateFlow<Float>? = null
) : SnackbarVisuals {
    override val duration: SnackbarDuration = SnackbarDuration.Indefinite
    override val actionLabel: String? = null
    override val withDismissAction: Boolean = false
}

open class SnackbarVisualsWithUndo(
    override val message: String,
    context: Context
) : SnackbarVisuals {
    override val actionLabel: String? = context.resources.getString(R.string.undo)
    override val duration: SnackbarDuration = SnackbarDuration.Long
    override val withDismissAction: Boolean = false
}
