package com.github.pwoicik.uekschedule.features.search.presentation.screens.search.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@NonRestartableComposable
fun SearchItemsColumn(
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.fillMaxSize(),
        content = content
    )
}
