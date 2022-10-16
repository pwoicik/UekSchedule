package com.github.pwoicik.uekschedule.features.search.presentation.screens.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchItem(
    itemName: String,
    isItemButtonEnabled: Boolean,
    onItemClick: () -> Unit,
    onSaveItemClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .clickable(
                enabled = isItemButtonEnabled,
                onClickLabel = stringResource(R.string.preview_group),
                onClick = onItemClick
            )
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = itemName,
                modifier = Modifier.weight(1f, fill = true)
            )

            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onClick = onSaveItemClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.save_group),
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp)
                )
            }

            Box(modifier = Modifier.padding(start = 4.dp)) {
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = stringResource(R.string.preview_group),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
    Divider(thickness = Dp.Hairline)
}
