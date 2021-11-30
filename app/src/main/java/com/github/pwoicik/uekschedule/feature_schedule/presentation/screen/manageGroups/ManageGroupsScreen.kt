package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.manageGroups

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.Screen
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsHeight

@Composable
fun ManageGroupsScreen(
    navController: NavController,
    viewModel: ManageGroupsViewModel = hiltViewModel()
) {
    val savedGroups by viewModel.savedGroups.collectAsState()

    Scaffold(
        topBar = {
            Surface(
                elevation = 24.dp,
                modifier = Modifier
                    .statusBarsHeight(24.dp)
            ) {}
        }
    ) {
        Surface(modifier = Modifier
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
                        text = stringResource(R.string.your_groups),
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    IconButton(
                        onClick = {
                            navController.navigate(Screen.AddGroupsScreen.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_group),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                AnimatedVisibility(!savedGroups.isNullOrEmpty()) {
                    LazyColumn(modifier = Modifier.padding(horizontal = 12.dp)) {
                        items(savedGroups!!) { group ->
                            Surface(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier.padding(vertical = 16.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp, horizontal = 16.dp)
                                        .padding(start = 8.dp)
                                ) {
                                    Text(group.name)
                                    IconButton(
                                        onClick = {
                                            viewModel.deleteGroup(group)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = stringResource(R.string.delete_group)
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.navigationBarsHeight())
                        }
                    }
                }

                AnimatedVisibility(
                    visible = savedGroups != null && savedGroups!!.isEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(stringResource(R.string.no_saved_groups))
                    }
                }
            }
        }
    }
}
