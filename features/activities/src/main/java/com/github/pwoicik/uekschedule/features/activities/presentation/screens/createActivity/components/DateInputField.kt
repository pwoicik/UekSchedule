package com.github.pwoicik.uekschedule.features.activities.presentation.screens.createActivity.components

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.github.pwoicik.uekschedule.common.R
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.*
import java.time.format.DateTimeFormatter

@Composable
internal fun DateInputField(
    date: LocalDate?,
    onDateSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as AppCompatActivity

    val picker = MaterialDatePicker.Builder.datePicker()
        .apply {
            val millis = if (date != null) {
                ZonedDateTime.of(
                    LocalDateTime.of(date.year, date.month, date.dayOfMonth, 0, 0),
                    ZoneId.systemDefault()
                ).toInstant().toEpochMilli()
            } else {
                ZonedDateTime.now().toInstant().toEpochMilli()
            }
            setSelection(millis)
        }
        .build()
        .apply {
            addOnPositiveButtonClickListener {
                val localDate = Instant.ofEpochMilli(selection!!)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                onDateSelect(localDate)
            }
        }

    InputFieldWithDialog(
        value = date?.let { date.format(formatter) } ?: "",
        showDialog = {
            picker.show(activity.supportFragmentManager, picker.toString())
        },
        label = stringResource(R.string.start_date),
        placeholder = "dd/mm/yyyy",
        modifier = modifier
    )
}

private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
