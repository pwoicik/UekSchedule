package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.manageActivities

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.components.SimpleListScaffold
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.destinations.AddActivityScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Destination
@Composable
fun ManageActivitiesScreen(
    navigator: DestinationsNavigator,
    viewModel: ManageActivitiesViewModel = hiltViewModel()
) {
    val activities by viewModel.activities.collectAsState()

    SimpleListScaffold(
        title = stringResource(R.string.other_activities),
        items = activities,
        emptyListMessage = stringResource(R.string.no_activities),
        onAddItemContentDescription = stringResource(R.string.add_activity),
        onAddItem = {
            navigator.navigate(AddActivityScreenDestination())
        },
        onClickItem = { activity ->
            navigator.navigate(AddActivityScreenDestination(activity.id))
        }
    ) { activity ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .padding(start = 8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .weight(
                        weight = 1f,
                        fill = true
                    )
            ) {
                Text(
                    text = activity.name,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Medium
                )
                if (activity.type != null) Text(activity.type)
                val time = activity.startDateTime.format(timeFormatter)
                val date = if (activity.repeatOnDaysOfWeek != null) {
                    activity.repeatOnDaysOfWeek
                        .joinToString(", ") {
                            it.getDisplayName(
                                TextStyle.SHORT_STANDALONE,
                                Locale.getDefault()
                            )
                        }
                } else {
                    activity.startDateTime.format(dateFormatter)
                }
                Text("$time, $date")
            }
            IconButton(
                onClick = {
                    viewModel.deleteActivity(activity)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.delete_activity)
                )
            }
        }
    }
}

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
