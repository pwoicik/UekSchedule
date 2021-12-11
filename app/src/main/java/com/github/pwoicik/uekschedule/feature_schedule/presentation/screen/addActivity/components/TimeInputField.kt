package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity.components

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.github.pwoicik.uekschedule.R
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TimeInputField(
    time: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as AppCompatActivity

    val picker = MaterialTimePicker.Builder()
        .apply {
            setTimeFormat(TimeFormat.CLOCK_24H)
            if (time != null) {
                setHour(time.hour)
                setMinute(time.minute)
            } else {
                val timeNow = LocalTime.now()
                setHour(timeNow.hour)
                setMinute(timeNow.minute)
            }
        }
        .build()
        .apply {
            addOnPositiveButtonClickListener {
                onTimeSelected(LocalTime.of(hour, minute))
            }
        }

    InputFieldWithDialog(
        value = time?.let { time.format(formatter) } ?: "",
        showDialog = {
            picker.show(activity.supportFragmentManager, picker.toString())
        },
        label = stringResource(R.string.start_time),
        placeholder = "hh:mm",
        modifier = modifier
    )
}

private val formatter = DateTimeFormatter.ofPattern("HH:mm")
