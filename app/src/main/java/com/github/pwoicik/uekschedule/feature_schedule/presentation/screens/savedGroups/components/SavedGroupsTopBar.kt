package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.savedGroups.components

import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalViewConfiguration
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
                    TitleItem(
                        title = stringResource(R.string.your_groups),
                        isActive = currentScreen == 0,
                        onClick = { onScreenChange(0) },
                        scale = titleScale.value,
                        alpha = titleScale.value
                    )
                    TitleItem(
                        title = stringResource(R.string.other_activities),
                        isActive = currentScreen == 1,
                        onClick = { onScreenChange(1) },
                        scale = 1f - titleScale.value + minTitleScale,
                        alpha = 1f - titleScale.value + minTitleScale
                    )
                }
            }

            IconButton(onClick = onAddItem) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = onAddItemContentDescription,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun TitleItem(
    title: String,
    isActive: Boolean,
    onClick: () -> Unit,
    scale: Float,
    alpha: Float,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .heightIn(min = LocalViewConfiguration.current.minimumTouchTargetSize.height)
            .alpha(alpha)
            .clip(RoundedCornerShape(20))
            .clickable(enabled = !isActive, onClick = onClick)
            .animateContentSize()
    ) {
        Text(
            text = title,
            maxLines = 1,
            modifier = Modifier.scale(scale)
        )
        AnimatedVisibility(
            visible = !isActive,
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut() + slideOutHorizontally()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 12.dp)
            )
        }
    }
}
