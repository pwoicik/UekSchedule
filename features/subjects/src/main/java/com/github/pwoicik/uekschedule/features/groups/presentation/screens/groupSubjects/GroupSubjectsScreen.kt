package com.github.pwoicik.uekschedule.features.groups.presentation.screens.groupSubjects

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.pwoicik.uekschedule.common.R
import com.github.pwoicik.uekschedule.domain.model.Subject
import com.github.pwoicik.uekschedule.presentation.util.zero
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Destination(
    navArgsDelegate = GroupSubjectsNavArgs::class
)
@Composable
fun GroupSubjectsScreen() {
    val viewModel: GroupSubjectsViewModel = hiltViewModel()
    val subjects by viewModel.subjects.collectAsState()

    Scaffold(
        contentWindowInsets = WindowInsets.zero()
    ) { innerPadding ->
        Crossfade(
            targetState = subjects == null,
            modifier = Modifier.padding(innerPadding)
        ) { isFetching ->
            when (isFetching) {
                true -> {}
                false -> {
                    SubjectList(
                        subjects = subjects!!,
                        onSubjectIgnoreClick = { viewModel.toggleSubjectIgnore(it) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SubjectList(
    subjects: List<Subject>,
    onSubjectIgnoreClick: (Subject) -> Unit
) {
    if (subjects.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Nothing here")
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(24.dp)
        ) {
            items(subjects) { subject ->
                Surface(
                    tonalElevation = 4.dp,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 10.dp)
                            .padding(start = 8.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(subject.name)
                            Text(
                                text = subject.type,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        IconButton(onClick = { onSubjectIgnoreClick(subject) }) {
                            AnimatedContent(
                                targetState = subject.isIgnored,
                                transitionSpec = {
                                    fadeIn(tween(200)) with fadeOut(tween(200))
                                }
                            ) { isIgnored ->
                                if (isIgnored) {
                                    Icon(
                                        imageVector = Icons.Default.VisibilityOff,
                                        contentDescription = stringResource(R.string.show_subject)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = stringResource(R.string.hide_subject)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
