package com.github.pwoicik.uekschedule.feature_schedule.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SnackbarWithError(
    snackbarData: SnackbarData,
    padding: PaddingValues = PaddingValues()
) {
    Snackbar(
        snackbarData = snackbarData,
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        modifier = Modifier.padding(padding)
    )
}

@Composable
fun SnackbarWithSuccess(
    snackbarData: SnackbarData,
    padding: PaddingValues = PaddingValues()
) {
    Snackbar(
        snackbarData = snackbarData,
        modifier = Modifier.padding(padding)
    )
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
    override val message: String,
) : SnackbarVisuals {
    override val duration: SnackbarDuration
        get() = SnackbarDuration.Short
    override val actionLabel: String?
        get() = null
    override val withDismissAction: Boolean
        get() = false
}
