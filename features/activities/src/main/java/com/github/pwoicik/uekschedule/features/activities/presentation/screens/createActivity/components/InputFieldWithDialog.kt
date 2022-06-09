package com.github.pwoicik.uekschedule.features.activities.presentation.screens.createActivity.components

import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.github.pwoicik.uekschedule.presentation.components.FormTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun InputFieldWithDialog(
    value: String,
    showDialog: () -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    LaunchedEffect(Unit) {
        var press = false
        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    press = true
                }
                is FocusInteraction.Focus -> {
                    if (!press)
                        interactionSource.emit(
                            PressInteraction.Release(
                                PressInteraction.Press(Offset.Unspecified)
                            )
                        )
                }
                is PressInteraction.Release -> {
                    showDialog()
                    press = false
                }
            }
        }
    }

    FormTextField(
        value = value,
        onValueChange = {},
        placeholder = placeholder,
        label = label,
        isError = value.isEmpty(),
        readOnly = true,
        singleLine = true,
        interactionSource = interactionSource,
        modifier = modifier
    )
}
