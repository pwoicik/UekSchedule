package com.github.pwoicik.uekschedule.feature_schedule.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun <T> SimpleList(
    items: List<T>,
    emptyListMessage: String,
    itemTitle: @Composable (T) -> Unit,
    itemActions: @Composable RowScope.(T) -> Unit,
    modifier: Modifier = Modifier,
    onItemClick: (T) -> Unit,
) {
    Surface(modifier = modifier) {
        Crossfade(items.isEmpty()) {
            when (it) {
                true -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(emptyListMessage)
                    }
                }
                false -> {
                    LazyColumn(contentPadding = PaddingValues(horizontal = 24.dp)) {
                        items(items) { item ->
                            Surface(
                                tonalElevation = 8.dp,
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(24.dp))
                                        .clickable {
                                            onItemClick(item)
                                        }
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp, horizontal = 16.dp)
                                            .padding(start = 8.dp)
                                    ) {
                                        itemTitle(item)
                                        itemActions(item)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
