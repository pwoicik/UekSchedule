package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.rememberScaffoldState
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
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity.components.AddActivityScaffold
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity.components.AddActivityTextFiled
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity.components.RepeatActivityInputColumn
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity.components.TimeInputField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Destination(
    navArgsDelegate = AddActivityNavArgs::class
)
@Composable
fun AddActivityScreen(
    navigator: DestinationsNavigator,
    viewModel: AddActivityViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val scaffoldState = rememberScaffoldState()

    val snackbarErrorMessage = stringResource(R.string.activity_data_error)
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddActivityViewModel.UiEvent.ShowError -> {
                    scaffoldState.snackbarHostState.showSnackbar(snackbarErrorMessage)
                }
                is AddActivityViewModel.UiEvent.ActivitySaved -> {
                    navigator.navigateUp()
                }
            }
        }
    }

    AddActivityScaffold(
        scaffoldState = scaffoldState,
        viewModel = viewModel
    ) {
        state?.let { state ->
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            AddActivityTextFiled(
                value = state.name,
                onValueChange = { viewModel.event(AddActivityScreenEvent.NameChanged(it)) },
                label = stringResource(R.string.activity_name),
                isRequired = true,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            AddActivityTextFiled(
                value = state.location,
                onValueChange = { viewModel.event(AddActivityScreenEvent.LocationChanged(it)) },
                label = stringResource(R.string.location),
                placeholder = stringResource(R.string.optional),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            AddActivityTextFiled(
                value = state.type,
                onValueChange = { viewModel.event(AddActivityScreenEvent.TypeChanged(it)) },
                label = stringResource(R.string.activity_type),
                placeholder = stringResource(R.string.optional),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            AddActivityTextFiled(
                value = state.teacher,
                onValueChange = { viewModel.event(AddActivityScreenEvent.TeacherChanged(it)) },
                label = stringResource(R.string.teacher),
                placeholder = stringResource(R.string.optional),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            TimeInputField(
                time = state.startTime,
                onTimeSelected = { lt ->
                    viewModel.event(
                        AddActivityScreenEvent.StartTimeChanged(lt)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            AddActivityTextFiled(
                value = state.durationMinutes,
                onValueChange = {
                    viewModel.event(
                        AddActivityScreenEvent.DurationMinutesChanged(it)
                    )
                },
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
                viewModel = viewModel,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
    }
}
