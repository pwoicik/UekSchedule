package com.github.pwoicik.uekschedule.features.activities.presentation.screens.createActivity.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.common.R
import com.github.pwoicik.uekschedule.presentation.util.zero

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CreateActivityScaffold(
    snackbarHostState: SnackbarHostState,
    onSaveChanges: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.zero(),
        topBar = {
            Surface {
                Text(
                    text = stringResource(R.string.create_new_activity),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onSaveChanges) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = stringResource(R.string.save_activity)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
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
