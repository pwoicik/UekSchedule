package com.github.pwoicik.uekschedule.feature_schedule.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    // TODO: disabled colors
    androidx.compose.material.Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = MaterialTheme.colorScheme.inversePrimary,
            uncheckedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
            uncheckedTrackColor = MaterialTheme.colorScheme.onSurface,
            disabledCheckedThumbColor = MaterialTheme.colorScheme.tertiary,
            disabledCheckedTrackColor = MaterialTheme.colorScheme.tertiary,
            disabledUncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
            disabledUncheckedTrackColor = MaterialTheme.colorScheme.tertiary
        ),
        modifier = modifier
    )
}
