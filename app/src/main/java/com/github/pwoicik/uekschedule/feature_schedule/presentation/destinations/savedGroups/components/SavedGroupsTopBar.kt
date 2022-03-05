package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.savedGroups.components

import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun SavedGroupsTopBar(
    currentScreen: Int,
    onScreenChange: (Int) -> Unit,
    onAddItem: () -> Unit,
    onAddItemContentDescription: String,
) {
    val minTitleScale = 0.7f
    val titleScale = remember { Animatable(1f) }
    LaunchedEffect(currentScreen) {
        titleScale.animateTo(
            if (currentScreen == 0) 1f else minTitleScale
        )
    }

    Surface {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(vertical = 24.dp, horizontal = 8.dp)
        ) {
            ProvideTextStyle(MaterialTheme.typography.headlineSmall) {
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f, fill = true)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .scale(titleScale.value)
                            .alpha(titleScale.value)
                            .clickable { onScreenChange(0) }
                    ) {
                        Text(text = stringResource(R.string.your_groups))
                        AnimatedVisibility(
                            visible = currentScreen != 0,
                            enter = fadeIn() + slideInHorizontally(),
                            exit = fadeOut() + slideOutHorizontally()
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForwardIos,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(start = 12.dp)
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .scale(1f - titleScale.value + minTitleScale)
                            .alpha(1f - titleScale.value + minTitleScale)
                            .clickable { onScreenChange(1) }
                    ) {
                        Text(text = stringResource(R.string.other_activities))
                        AnimatedVisibility(
                            visible = currentScreen != 1,
                            enter = fadeIn() + slideInHorizontally(),
                            exit = fadeOut() + slideOutHorizontally()
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForwardIos,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(start = 12.dp)
                            )
                        }
                    }
                }
            }

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
    }
}