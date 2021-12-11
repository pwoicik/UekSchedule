package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity.AddActivityScreenEvent
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity.AddActivityScreenState
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity.AddActivityViewModel
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

@Composable
fun RepeatActivityInputColumn(
    state: AddActivityScreenState,
    viewModel: AddActivityViewModel
) {
    Column(
        modifier = Modifier
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.repeat_activity),
                fontWeight = FontWeight.Medium
            )
            Switch(
                checked = state.repeatActivity,
                onCheckedChange = {
                    viewModel.event(AddActivityScreenEvent.RepeatActivityChanged)
                },
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = MaterialTheme.colors.onSurface,
                    checkedTrackColor = MaterialTheme.colors.secondary,
                    checkedThumbColor = MaterialTheme.colors.secondary
                )
            )
        }

        Crossfade(state.repeatActivity) {
            when (it) {
                true -> {
                    DaysOfWeekSelectionRow(
                        viewModel = viewModel,
                        selectedDays = state.repeatOnDaysOfWeek
                    )
                }
                false -> {
                    DateInputField(
                        date = state.startDate,
                        onDateSelected = { ld ->
                            viewModel.event(
                                AddActivityScreenEvent.StartDateChanged(ld)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun DaysOfWeekSelectionRow(
    viewModel: AddActivityViewModel,
    selectedDays: Set<DayOfWeek>
) {
    val daysOfWeek by remember {
        derivedStateOf {
            DayOfWeek.values().map {
                it to it.getDisplayName(
                    TextStyle.SHORT_STANDALONE,
                    Locale.getDefault()
                )
            }.toMap()
        }
    }

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .run {
                if (selectedDays.isEmpty())
                    border(
                        width = 1.dp,
                        color = MaterialTheme.colors.error,
                        shape = RoundedCornerShape(10.dp)
                    )
                else this
            }
    ) {
        FlowRow(
            mainAxisSize = SizeMode.Expand,
            mainAxisAlignment = FlowMainAxisAlignment.SpaceEvenly,
            crossAxisSpacing = 4.dp,
            modifier = Modifier
                .selectableGroup()
                .padding(8.dp)
        ) {
            for ((dayOfWeek, displayName) in daysOfWeek) {
                val isSelected = dayOfWeek in selectedDays
                val color = animateColorAsState(
                    if (isSelected) {
                        MaterialTheme.colors.secondary
                    } else {
                        MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                    }
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = color.value,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .selectable(
                            selected = isSelected,
                            role = Role.Checkbox
                        ) {
                            if (isSelected) {
                                viewModel.event(
                                    AddActivityScreenEvent.RemoveDayOfWeekToRepeat(dayOfWeek)
                                )
                            } else {
                                viewModel.event(
                                    AddActivityScreenEvent.AddDayOfWeekToRepeat(dayOfWeek)
                                )
                            }
                        }
                ) {
                    Text(
                        text = displayName,
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
