package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
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
import androidx.navigation.NavController
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity.components.AddActivityTextFiled
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity.components.RepeatActivityInputColumn
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity.components.TimeInputField
import com.google.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddActivityScreen(
    navController: NavController,
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
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .statusBarsHeight(24.dp)
                )
                Text(
                    text = stringResource(R.string.create_new_activity),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        },
        bottomBar = {
            BottomAppBar(
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        viewModel.event(AddActivityScreenEvent.SaveActivity)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = stringResource(R.string.save_activity)
                    )
                }
            }
        }
    ) { innerPadding ->
        state?.let { state ->
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(
                    top = 0.dp,
                    bottom = 24.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                item {
                    AddActivityTextFiled(
                        value = state.name,
                        onValueChange = { viewModel.event(AddActivityScreenEvent.NameChanged(it)) },
                        label = stringResource(R.string.activity_name),
                        isRequired = true,
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .fillMaxWidth()
                    )
                }

                item {
                    AddActivityTextFiled(
                        value = state.location,
                        onValueChange = { viewModel.event(AddActivityScreenEvent.LocationChanged(it)) },
                        label = stringResource(R.string.location),
                        placeholder = stringResource(R.string.optional),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                item {
                    AddActivityTextFiled(
                        value = state.type,
                        onValueChange = { viewModel.event(AddActivityScreenEvent.TypeChanged(it)) },
                        label = stringResource(R.string.activity_type),
                        placeholder = stringResource(R.string.optional),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                item {
                    AddActivityTextFiled(
                        value = state.teacher,
                        onValueChange = { viewModel.event(AddActivityScreenEvent.TeacherChanged(it)) },
                        label = stringResource(R.string.teacher),
                        placeholder = stringResource(R.string.optional),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                item {
                    TimeInputField(
                        time = state.startTime,
                        onTimeSelected = { lt ->
                            viewModel.event(
                                AddActivityScreenEvent.StartTimeChanged(lt)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                item {
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
                    )
                }

                item {
                    Divider(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                    )
                    RepeatActivityInputColumn(state = state, viewModel = viewModel)
                }
            }
        }
    }
}
