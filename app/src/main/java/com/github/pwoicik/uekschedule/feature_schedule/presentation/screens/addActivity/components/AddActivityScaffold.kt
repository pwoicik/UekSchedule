package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.addActivity.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.addActivity.AddActivityScreenEvent
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.addActivity.AddActivityViewModel
import com.google.accompanist.insets.statusBarsHeight

@Composable
fun AddActivityScaffold(
    scaffoldState: ScaffoldState,
    viewModel: AddActivityViewModel,
    content: @Composable ColumnScope.() -> Unit
) {
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
        Surface(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                content()
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
