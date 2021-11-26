package com.github.pwoicik.uekschedule.screen.addGroupsScreen

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.components.LoadingSpinnerCentered
import com.github.pwoicik.uekschedule.database.Group
import com.github.pwoicik.uekschedule.database.ScheduleViewModel
import kotlinx.coroutines.launch

@Composable
fun AddGroupsScreen(
    viewModel: ScheduleViewModel,
    goBack: () -> Unit
) {
    val savedGroups by viewModel.groups.collectAsState()
    var availableGroups: List<Group> by remember { mutableStateOf(emptyList()) }

    var isFetchingAvailableGroups by remember { mutableStateOf(false) }

    val scaffoldState = rememberScaffoldState()

    val connectionErrorMessage = stringResource(R.string.couldnt_connect)
    val coroutineScope = rememberCoroutineScope()
    val snackbarCoroutineScope = rememberCoroutineScope()
    val fetchAvailableGroups: () -> Unit = {
        coroutineScope.launch {
            try {
                isFetchingAvailableGroups = true
                availableGroups = viewModel.getAvailableGroups()

                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
            } catch (e: Exception) {
                if (scaffoldState.snackbarHostState.currentSnackbarData == null)
                    snackbarCoroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = connectionErrorMessage,
                            duration = SnackbarDuration.Indefinite
                        )
                    }
            } finally {
                isFetchingAvailableGroups = false
            }
        }
    }

    SideEffect {
        fetchAvailableGroups()
    }

    var selectedGroups by remember { mutableStateOf(emptyList<Group>()) }

    var isFetchingNewGroups by remember { mutableStateOf(false) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                title = {},
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "")
                    }
                },
                actions = {
                    IconButton(
                        enabled = selectedGroups.isNotEmpty(),
                        onClick = { selectedGroups = emptyList() }
                    ) {
                        Icon(Icons.Filled.Clear, contentDescription = "")
                    }

                    if (isFetchingNewGroups) {
                        val transition = rememberInfiniteTransition()
                        val spin by transition.animateFloat(
                            initialValue = 0f,
                            targetValue = 360f,
                            animationSpec = infiniteRepeatable(tween(1000))
                        )

                        // Just for laying out the icon correctly
                        IconButton(onClick = { }, modifier = Modifier.rotate(spin)) {
                            Icon(
                                imageVector = Icons.Filled.Sync,
                                contentDescription = "",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    } else
                        IconButton(
                            enabled = selectedGroups.isNotEmpty(),
                            onClick = {
                                coroutineScope.launch {
                                    isFetchingNewGroups = true
                                    viewModel.addSchedules(selectedGroups).join()
                                    isFetchingNewGroups = false
                                    goBack()
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Done, contentDescription = "")
                        }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(it) { snackbarData ->
                Snackbar(
                    shape = RoundedCornerShape(8.dp),
                    action = {
                        IconButton(onClick = fetchAvailableGroups) {
                            Icon(Icons.Filled.Refresh, contentDescription = "")
                        }
                    },
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(snackbarData.message)
                }
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                if (isFetchingAvailableGroups || savedGroups == null) {
                    LoadingSpinnerCentered()
                } else {
                    var inputText by remember { mutableStateOf("") }

                    AddGroupsScreenContent(
                        availableGroups = availableGroups,
                        savedGroups = savedGroups!!,
                        inputText = inputText,
                        onInputChange = { inputText = it },
                        selectedGroups = selectedGroups,
                        onSelectGroup = { selectedGroups = selectedGroups + it },
                        onUnselectGroup = { selectedGroups = selectedGroups - it }
                    )
                }
            }
        }
    }
}

@Composable
fun ColumnScope.AddGroupsScreenContent(
    availableGroups: List<Group>,
    savedGroups: List<Group>,
    inputText: String,
    onInputChange: (String) -> Unit,
    selectedGroups: List<Group>,
    onSelectGroup: (Group) -> Unit,
    onUnselectGroup: (Group) -> Unit
) {
    SearchAvailableGroupsTextField(
        inputText = inputText,
        onInputChange = onInputChange
    )

    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
            .padding(top = 24.dp)
    )

    AvailableGroups(
        availableGroups = availableGroups,
        savedGroups = savedGroups,
        searchText = inputText,
        selectedGroups = selectedGroups,
        onSelectGroup = onSelectGroup,
        onUnselectGroup = onUnselectGroup
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchAvailableGroupsTextField(
    inputText: String,
    onInputChange: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    SideEffect {
        focusRequester.requestFocus()
    }

    TextField(
        value = inputText,
        onValueChange = onInputChange,
        placeholder = {
            Text(stringResource(R.string.add_group_textfield_placeholder))
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
    )
}

@Composable
fun ColumnScope.AvailableGroups(
    availableGroups: List<Group>,
    savedGroups: List<Group>,
    searchText: String,
    selectedGroups: List<Group>,
    onSelectGroup: (Group) -> Unit,
    onUnselectGroup: (Group) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {
        val filteredGroups = availableGroups.filter {
            val isNotSelected = it !in selectedGroups
            val matchesSearchText = it.name.lowercase().contains(searchText.lowercase())

            isNotSelected && matchesSearchText
        }

        if (selectedGroups.isNotEmpty()) {
            SelectedGroupsRow(
                selectedGroups = selectedGroups,
                onUnselectGroup = onUnselectGroup
            )
        }
        if (filteredGroups.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Text(stringResource(R.string.no_group_found))
            }
        } else {
            AvailableGroupsList(
                availableGroups = filteredGroups,
                savedGroups = savedGroups,
                onSelectGroup = onSelectGroup,
            )
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectedGroupsRow(
    selectedGroups: List<Group>,
    onUnselectGroup: (Group) -> Unit
) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        for (i in selectedGroups.lastIndex downTo 0) {
            val group = selectedGroups[i]
            item {
                Card(
                    border = BorderStroke(1.dp, MaterialTheme.colors.onSurface),
                    onClick = { onUnselectGroup(group) },
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .alpha(0.5f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 2.dp, horizontal = 6.dp)
                            .height(IntrinsicSize.Min)
                    ) {
                        ProvideTextStyle(MaterialTheme.typography.body2) {
                            Text(group.name)
                            Icon(Icons.Filled.Clear, contentDescription = "")
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun AvailableGroupsList(
    availableGroups: List<Group>,
    savedGroups: List<Group>,
    onSelectGroup: (Group) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(
            items = availableGroups,
            key = Group::id
        ) { group ->
            val isAlreadySaved = group in savedGroups

            AvailableGroupsListItem(
                groupName = group.name,
                isAlreadySaved = isAlreadySaved,
                alpha = if (isAlreadySaved) 0.5f else 1f,
                onSelectGroup = {
                    onSelectGroup(group)
                }
            )
        }
    }
}

@Composable
fun AvailableGroupsListItem(
    groupName: String,
    isAlreadySaved: Boolean,
    alpha: Float,
    onSelectGroup: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .selectable(
                selected = false,
                enabled = !isAlreadySaved,
                onClick = onSelectGroup
            )
    ) {
        Text(
            text = if (isAlreadySaved)
                stringResource(R.string.group_saved, groupName)
            else
                groupName,
            modifier = Modifier.padding(8.dp)
        )
    }
}
