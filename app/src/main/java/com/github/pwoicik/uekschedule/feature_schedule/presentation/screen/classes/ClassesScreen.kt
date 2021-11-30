package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.classes

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.classes.components.ClassesList
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.classes.components.ClassesScaffold
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ClassesScreen(
    navController: NavController,
    viewModel: ClassesViewModel = hiltViewModel()
) {
    val classes by viewModel.classes
    val isUpdating by viewModel.isUpdating

    val timeNow by viewModel.timeFlow.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val snackbarMessage = stringResource(R.string.couldnt_connect)

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ClassesViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = snackbarMessage,
                        duration = SnackbarDuration.Indefinite
                    )
                }
                ClassesViewModel.UiEvent.HideSnackbar -> {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                }
            }
        }
    }

    ClassesScaffold(
        scaffoldState = scaffoldState,
        isUpdating = isUpdating,
        navController = navController,
        viewModel = viewModel
    ) {
        AnimatedVisibility(
            visible = classes != null && classes!!.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(stringResource(R.string.no_classes_message))
            }
        }

        AnimatedVisibility(
            visible = classes != null && classes!!.isNotEmpty(),
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            ClassesList(
                isUpdating = isUpdating,
                classes = classes!!,
                timeNow = timeNow,
                onRefresh = viewModel::updateClasses
            )
        }
    }
}
