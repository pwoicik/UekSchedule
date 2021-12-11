package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity.components

import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.collectLatest

@Composable
fun InputFieldWithDialog(
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

    OutlinedTextField(
        value = value,
        onValueChange = {},
        placeholder = {
            Text(placeholder)
        },
        label = {
            Text(label)
        },
        isError = value.isEmpty(),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            focusedIndicatorColor = MaterialTheme.colors.secondary,
            cursorColor = MaterialTheme.colors.secondary,
            focusedLabelColor = MaterialTheme.colors.secondary
        ),
        readOnly = true,
        singleLine = true,
        interactionSource = interactionSource,
        modifier = modifier
    )
}
