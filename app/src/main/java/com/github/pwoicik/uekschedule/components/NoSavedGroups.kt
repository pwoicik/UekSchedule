package com.github.pwoicik.uekschedule.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R

@Composable
fun NoSavedGroups(onAddGroups: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.no_saved_groups))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAddGroups) {
            Text(stringResource(R.string.add_group))
        }
    }
}
