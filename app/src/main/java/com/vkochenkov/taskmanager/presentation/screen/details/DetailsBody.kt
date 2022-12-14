package com.vkochenkov.taskmanager.presentation.screen.details

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vkochenkov.taskmanager.R
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.components.ErrorState
import com.vkochenkov.taskmanager.presentation.theme.TaskManagerTheme
import com.vkochenkov.taskmanager.presentation.utils.getColor
import com.vkochenkov.taskmanager.presentation.utils.getNameForUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsBody(
    state: DetailsBodyState,
    onAction: (DetailsActions) -> Unit
) {

    BackHandler(enabled = true, onBack = {
        onAction.invoke(DetailsActions.BackPressed())
    })

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onAction.invoke(DetailsActions.BackPressed())
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                onAction.invoke(DetailsActions.SaveTask)
                            },
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_baseline_save_24),
                                    contentDescription = null
                                )
                            }
                        )
                        IconButton(
                            onClick = {
                                onAction.invoke(DetailsActions.DeleteTask(true))
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                )
            }
        ) { padding ->
            when (state) {
                is DetailsBodyState.Content -> {
                    ContentState(
                        padding,
                        state.task,
                        state.showDialogOnBack,
                        state.showDialogOnDelete,
                        state.showTitleValidation,
                        state.showDescriptionValidation,
                        onAction
                    )
                }
                is DetailsBodyState.Error -> ErrorState(padding)
                is DetailsBodyState.Loading -> LoadingState(padding)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentState(
    padding: PaddingValues,
    task: Task,
    showDialogOnBack: Boolean,
    showDialogOnDelete: Boolean,
    showTitleValidation: Boolean,
    showDescriptionValidation: Boolean,
    onAction: (DetailsActions) -> Unit
) {
    var priorityDropDownIsExpanded by remember { mutableStateOf(false) }
    var statusDropDownIsExpanded by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description ?: "") }

    if (showDialogOnBack) {
        AlertDialog(
            onDismissRequest = {
                onAction.invoke(DetailsActions.CancelBackDialog)
            },
            title = {
                Text(text = stringResource(R.string.details_save_dialog_title))
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAction.invoke(DetailsActions.SaveTask)
                    }
                ) {
                    Text(stringResource(R.string.details_save_dialog_confirm_btn))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onAction.invoke(DetailsActions.BackPressed(false))
                    }) {
                    Text(stringResource(R.string.details_save_dialog_dismiss_btn))
                }
            }
        )
    }

    if (showDialogOnDelete) {
        AlertDialog(
            onDismissRequest = {
                onAction.invoke(DetailsActions.CancelDeleteDialog)
            },
            title = {
                Text(text = stringResource(R.string.details_delete_dialog_title))
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAction.invoke(DetailsActions.DeleteTask(false))
                    }) {
                    Text(stringResource(R.string.details_delete_dialog_confirm_btn))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onAction.invoke(DetailsActions.CancelDeleteDialog)
                    }) {
                    Text(stringResource(R.string.details_delete_dialog_dismiss_btn))
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        item {
            OutlinedTextField(
                isError = showTitleValidation,
                label = {
                    Text(
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                        text = stringResource(R.string.details_title_hint)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = {
                    title = it
                    onAction.invoke(DetailsActions.TaskChanged(task.copy(title = it)))
                })

            Spacer(modifier = Modifier.size(8.dp))
        }

        item {
            OutlinedTextField(
                isError = showDescriptionValidation,
                label = {
                    Text(
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                        text = stringResource(R.string.details_description_hint)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = {
                    description = it
                    onAction.invoke(DetailsActions.TaskChanged(task.copy(description = it)))
                }
            )

            Spacer(modifier = Modifier.size(8.dp))
        }

        item {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    priorityDropDownIsExpanded = !priorityDropDownIsExpanded
                }
                .padding(vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(R.string.details_priority) + task.priority.toString(),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .border(border = BorderStroke(1.dp, Color.Black), shape = CircleShape)
                            .background(color = task.priority.getColor(), shape = CircleShape)
                    )
                }
            }

            DropdownMenu(
                expanded = priorityDropDownIsExpanded,
                onDismissRequest = { priorityDropDownIsExpanded = false }
            ) {
                Task.Priority.values().forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            onAction.invoke(DetailsActions.TaskChanged(task.copy(priority = item)))
                            priorityDropDownIsExpanded = false
                        },
                        interactionSource = MutableInteractionSource(),
                        text = {
                            Text(text = item.toString())
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.size(8.dp))
        }

        item {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    statusDropDownIsExpanded = !statusDropDownIsExpanded
                }
                .padding(vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.details_status) + task.status.getNameForUi(
                        LocalContext.current
                    ),
                    fontSize = 14.sp
                )
            }

            DropdownMenu(
                expanded = statusDropDownIsExpanded,
                onDismissRequest = { statusDropDownIsExpanded = false }
            ) {
                Task.Status.values().forEach { status ->
                    DropdownMenuItem(
                        onClick = {
                            onAction.invoke(DetailsActions.TaskChanged(task.copy(status = status)))
                            statusDropDownIsExpanded = false
                        },
                        interactionSource = MutableInteractionSource(),
                        text = {
                            Text(text = status.getNameForUi(LocalContext.current))
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

@Composable
private fun LoadingState(
    padding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp)
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun Preview() {
    TaskManagerTheme {
        DetailsBody(
            DetailsBodyState.Content(
                Task(
                    0,
                    System.currentTimeMillis().toString(),
                    System.currentTimeMillis().toString(),
                    "1 number number number number number number number number number number number number number",
                    "dddd ddd dd",
                    Task.Priority.NORMAL,
                    Task.Status.IN_PROGRESS
                )
            )
        ) {}
    }
}