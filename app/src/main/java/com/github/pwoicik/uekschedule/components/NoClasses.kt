package com.github.pwoicik.uekschedule.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R

@OptIn(ExperimentalUnitApi::class)
@Composable
fun NoClasses() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.h5
        ) {
            Text(stringResource(R.string.no_classes_message_1))
            Text(stringResource(R.string.no_classes_message_2))
        }
        Text(
            text = "🎉",
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
