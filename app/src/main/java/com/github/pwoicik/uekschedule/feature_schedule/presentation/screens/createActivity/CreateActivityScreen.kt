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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.createActivity.components.CreateActivityScaffold
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.createActivity.components.CreateActivityTextField
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
        onSaveChanges = { viewModel.emit(CreateActivityEvent.SaveActivity) }
    ) {
        state?.let { state ->
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            CreateActivityTextField(
                value = state.name,
                onValueChange = { viewModel.emit(CreateActivityEvent.NameChanged(it)) },
                label = stringResource(R.string.activity_name),
                isRequired = true,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            CreateActivityTextField(
                value = state.location,
                onValueChange = { viewModel.emit(CreateActivityEvent.LocationChanged(it)) },
                label = stringResource(R.string.location),
                placeholder = stringResource(R.string.optional),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            CreateActivityTextField(
                value = state.type,
                onValueChange = { viewModel.emit(CreateActivityEvent.TypeChanged(it)) },
                label = stringResource(R.string.activity_type),
                placeholder = stringResource(R.string.optional),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            CreateActivityTextField(
                value = state.teacher,
                onValueChange = { viewModel.emit(CreateActivityEvent.TeacherChanged(it)) },
                label = stringResource(R.string.teacher),
                placeholder = stringResource(R.string.optional),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            TimeInputField(
                time = state.startTime,
                onTimeSelected = { lt -> viewModel.emit(CreateActivityEvent.StartTimeChanged(lt)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            CreateActivityTextField(
                value = state.durationMinutes,
                onValueChange = { viewModel.emit(CreateActivityEvent.DurationMinutesChanged(it)) },
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
                onRepeatActivityChange = { viewModel.emit(CreateActivityEvent.RepeatActivityChanged) },
                onStartDateSelect = { viewModel.emit(CreateActivityEvent.StartDateChanged(it)) },
                onSelectDayToRepeat = { viewModel.emit(CreateActivityEvent.AddDayOfWeekToRepeat(it)) },
                onUnselectDayToRepeat = {
                    viewModel.emit(
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
