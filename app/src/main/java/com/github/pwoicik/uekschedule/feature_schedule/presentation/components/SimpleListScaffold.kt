package com.github.pwoicik.uekschedule.feature_schedule.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsHeight

@Composable
fun <T> SimpleListScaffold(
    title: String,
    items: List<T>?,
    emptyListMessage: String,
    onAddItemContentDescription: String,
    onAddItem: () -> Unit,
    onClickItem: ((T) -> Unit)? = null,
    itemComposable: @Composable (T) -> Unit
) {
    Scaffold(
        topBar = {
            Spacer(
                modifier = Modifier
                    .statusBarsHeight(24.dp)
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp)
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    IconButton(
                        onClick = onAddItem
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = onAddItemContentDescription,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                items?.let { items ->
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
                                LazyColumn(modifier = Modifier.padding(horizontal = 12.dp)) {
                                    items(items) { item ->
                                        Surface(
                                            elevation = 8.dp,
                                            shape = RoundedCornerShape(24.dp),
                                            modifier = Modifier
                                                .padding(vertical = 16.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(24.dp))
                                                    .run {
                                                        if (onClickItem != null)
                                                            clickable {
                                                                onClickItem(item)
                                                            }
                                                        else this
                                                    }
                                            ) {
                                                itemComposable(item)
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
    }
}
