package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.createActivity

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.createActivity.components.CreateActivityScaffold
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.createActivity.components.CreateActivityTextFiled
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.createActivity.components.RepeatActivityInputColumn
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.createActivity.components.TimeInputField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Destination(
    navGraph = "mainScreen",
    navArgsDelegate = CreateActivityNavArgs::class
)
@Composable
fun CreateActivityScreen(
    navigator: DestinationsNavigator,
    viewModel: CreateActivityViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val snackbarHostState = remember { SnackbarHostState() }

    val snackbarErrorMessage = stringResource(R.string.activity_data_error)
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CreateActivityViewModel.UiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(snackbarErrorMessage)
                }
                is CreateActivityViewModel.UiEvent.ActivitySaved -> {
                    navigator.navigateUp()
                }
            }
        }
    }

    CreateActivityScaffold(
        snackbarHostState = snackbarHostState,
        onSaveChanges = { viewModel.event(CreateActivityEvent.SaveActivity) }
    ) {
        state?.let { state ->
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            CreateActivityTextFiled(
                value = state.name,
                onValueChange = { viewModel.event(CreateActivityEvent.NameChanged(it)) },
                label = stringResource(R.string.activity_name),
                isRequired = true,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            CreateActivityTextFiled(
                value = state.location,
                onValueChange = { viewModel.event(CreateActivityEvent.LocationChanged(it)) },
                label = stringResource(R.string.location),
                placeholder = stringResource(R.string.optional),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            CreateActivityTextFiled(
                value = state.type,
                onValueChange = { viewModel.event(CreateActivityEvent.TypeChanged(it)) },
                label = stringResource(R.string.activity_type),
                placeholder = stringResource(R.string.optional),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            CreateActivityTextFiled(
                value = state.teacher,
                onValueChange = { viewModel.event(CreateActivityEvent.TeacherChanged(it)) },
                label = stringResource(R.string.teacher),
                placeholder = stringResource(R.string.optional),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            TimeInputField(
                time = state.startTime,
                onTimeSelected = { lt -> viewModel.event(CreateActivityEvent.StartTimeChanged(lt)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            CreateActivityTextFiled(
                value = state.durationMinutes,
                onValueChange = { viewModel.event(CreateActivityEvent.DurationMinutesChanged(it)) },
                label = stringResource(R.string.duration),
                isRequired = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Divider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp)
            )
            RepeatActivityInputColumn(
                state = state,
                onRepeatActivityChange = { viewModel.event(CreateActivityEvent.RepeatActivityChanged) },
                onStartDateSelect = { viewModel.event(CreateActivityEvent.StartDateChanged(it)) },
                onSelectDayToRepeat = { viewModel.event(CreateActivityEvent.AddDayOfWeekToRepeat(it)) },
                onUnselectDayToRepeat = {
                    viewModel.event(
                        CreateActivityEvent.RemoveDayOfWeekToRepeat(
                            it
                        )
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
    }
}
