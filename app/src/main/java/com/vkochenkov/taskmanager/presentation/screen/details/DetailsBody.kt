package com.vkochenkov.taskmanager.presentation.screen.details

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vkochenkov.taskmanager.R
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.components.CustomScaffold
import com.vkochenkov.taskmanager.presentation.theme.TaskManagerTheme
import com.vkochenkov.taskmanager.presentation.utils.*

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
        CustomScaffold(
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
                                onAction.invoke(DetailsActions.ShowNotificationDialog)
                            },
                            content = {
                                Icon(
                                    painter = painterResource(
                                        if (state.task?.notificationTime != null) {
                                            R.drawable.ic_baseline_notifications_active_24
                                        } else {
                                            R.drawable.ic_baseline_notifications_24
                                        }
                                    ),
                                    contentDescription = null
                                )
                            }
                        )
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
            },
            isLoadingPage = state.isLoadingPage,
            isErrorPage = state.isErrorPage,
            isBlockInteractionFull = state.isLoadingPage
        ) { padding ->

            if (state.task != null) {

                var priorityDropDownIsExpanded by remember { mutableStateOf(false) }
                var statusDropDownIsExpanded by remember { mutableStateOf(false) }

                var title by remember { mutableStateOf(state.task.title) }
                var description by remember { mutableStateOf(state.task.description ?: "") }

                if (state.showOnBackDialog) {
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
                                    onAction.invoke(DetailsActions.BackPressed(false))
                                }) {
                                Text(stringResource(R.string.details_save_dialog_btn_dismiss))
                            }

                            Button(
                                onClick = {
                                    onAction.invoke(DetailsActions.SaveTask)
                                }
                            ) {
                                Text(stringResource(R.string.details_save_dialog_btn_confirm))
                            }
                        }
                    )
                }

                if (state.showOnDeleteDialog) {
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
                                    onAction.invoke(DetailsActions.CancelDeleteDialog)
                                }) {
                                Text(stringResource(R.string.details_delete_dialog_btn_dismiss))
                            }

                            Button(
                                onClick = {
                                    onAction.invoke(DetailsActions.DeleteTask(false))
                                }) {
                                Text(stringResource(R.string.details_delete_dialog_btn_confirm))
                            }
                        }
                    )
                }

                if (state.showNotificationDialog) {

                    var notificationTime by remember { mutableStateOf(state.task.getFormattedNotificationTime()) }
                    var notificationDate by remember { mutableStateOf(state.task.getFormattedNotificationDate()) }

                    AlertDialog(
                        onDismissRequest = {
                            onAction.invoke(DetailsActions.CancelNotificationDialog)
                        },
                        title = {
                            Text(text = stringResource(R.string.details_notification_dialog_title))
                        },
                        text = {
                            Column() {
                                OutlinedTextField(
                                    label = {
                                        Text(
                                            text = stringResource(R.string.details_notification_dialog_time_field_label)
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    value = notificationTime,
                                    onValueChange = {
                                        notificationTime = it
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            notificationTime =
                                                System.currentTimeMillis().getFormattedTime()
                                        }) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_baseline_access_time_24),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                )

                                OutlinedTextField(
                                    label = {
                                        Text(
                                            text = stringResource(R.string.details_notification_dialog_date_field_label)
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    value = notificationDate,
                                    onValueChange = {
                                        notificationDate = it
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            notificationDate =
                                                System.currentTimeMillis().getFormattedDate()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.DateRange,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    onAction.invoke(DetailsActions.CancelNotificationDialog)
                                }) {
                                Text(stringResource(R.string.details_notification_dialog_btn_cancel))
                            }

                            if (state.task.notificationTime != null) {
                                Button(
                                    onClick = {
                                        onAction.invoke(DetailsActions.RemoveNotification)
                                    }) {
                                    Text(stringResource(R.string.details_notification_dialog_btn_remove))
                                }
                            }

                            Button(
                                onClick = {
                                    onAction.invoke(
                                        DetailsActions.SetNotification(
                                            notificationTime,
                                            notificationDate
                                        )
                                    )
                                }) {
                                Text(stringResource(R.string.details_notification_dialog_btn_set))
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
                            isError = state.showTitleValidation,
                            label = {
                                if (state.showTitleValidation) {
                                    Text(
                                        text = stringResource(R.string.error_wrong_number),
                                        color = MaterialTheme.colorScheme.error,
                                    )
                                } else {
                                    Text(
                                        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                                        text = stringResource(R.string.details_title_hint)
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            value = title,
                            onValueChange = {
                                title = it
                                onAction.invoke(DetailsActions.TaskChanged(state.task.copy(title = it)))
                            })
                    }

                    item {
                        Spacer(modifier = Modifier.size(8.dp))
                    }

                    item {
                        OutlinedTextField(
                            isError = state.showDescriptionValidation,
                            label = {
                                if (state.showDescriptionValidation) {
                                    Text(
                                        text = stringResource(R.string.error_wrong_number),
                                        color = MaterialTheme.colorScheme.error,
                                    )
                                } else {
                                    Text(
                                        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                                        text = stringResource(R.string.details_description_hint)
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            value = description,
                            onValueChange = {
                                description = it
                                onAction.invoke(
                                    DetailsActions.TaskChanged(
                                        state.task.copy(
                                            description = it
                                        )
                                    )
                                )
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.size(16.dp))
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
                                    text = stringResource(R.string.details_priority) + state.task.priority.getNameForUi(),
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .border(
                                            border = BorderStroke(1.dp, Color.Black),
                                            shape = CircleShape
                                        )
                                        .background(
                                            color = state.task.priority.getColor(),
                                            shape = CircleShape
                                        )
                                )
                            }

                            DropdownMenu(
                                expanded = priorityDropDownIsExpanded,
                                onDismissRequest = { priorityDropDownIsExpanded = false }
                            ) {
                                Task.Priority.values().forEach { item ->
                                    DropdownMenuItem(
                                        onClick = {
                                            onAction.invoke(
                                                DetailsActions.TaskChanged(
                                                    state.task.copy(
                                                        priority = item
                                                    )
                                                )
                                            )
                                            priorityDropDownIsExpanded = false
                                        },
                                        interactionSource = MutableInteractionSource(),
                                        text = {
                                            Text(text = item.getNameForUi())
                                        }
                                    )
                                }
                            }
                        }
                    }

                    item {
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
                                text = stringResource(R.string.details_status) + state.task.status,
                                fontSize = 14.sp
                            )

                            DropdownMenu(
                                expanded = statusDropDownIsExpanded,
                                onDismissRequest = { statusDropDownIsExpanded = false }
                            ) {
                                state.statuses.forEach { status ->
                                    DropdownMenuItem(
                                        onClick = {
                                            onAction.invoke(
                                                DetailsActions.TaskChanged(
                                                    state.task.copy(
                                                        status = status
                                                    )
                                                )
                                            )
                                            statusDropDownIsExpanded = false
                                        },
                                        interactionSource = MutableInteractionSource(),
                                        text = {
                                            Text(text = status)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.size(8.dp))
                    }

                    item {
                        val launcher =
                            rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                                uri?.let { onAction.invoke(DetailsActions.AttachFile(uri)) }
                            }

                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                launcher.launch(arrayOf("*/*"))
                            }
                            .padding(vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_baseline_attachment_24),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(text = stringResource(R.string.details_attach_file_title))
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.size(8.dp))
                    }

                    item {
                        if (state.task.attachments.isNotEmpty()) {
                            Text(
                                text = stringResource(R.string.details_attachments_content),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            LazyRow {
                                state.task.attachments.forEach { attachment ->
                                    item {
                                        Card(
                                            modifier = Modifier.size(72.dp),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clickable {
                                                        onAction.invoke(
                                                            DetailsActions.OpenAttachment(
                                                                attachment
                                                            )
                                                        )
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.TopEnd
                                                ) {
                                                    IconButton(
                                                        modifier = Modifier.size(24.dp),
                                                        onClick = {
                                                            onAction.invoke(
                                                                DetailsActions.DeleteAttachment(
                                                                    attachment
                                                                )
                                                            )
                                                        }
                                                    ) {
                                                        Icon(
                                                            modifier = Modifier.size(16.dp),
                                                            imageVector = Icons.Default.Close,
                                                            contentDescription = null
                                                        )
                                                    }
                                                }
                                                val fileType = when {
                                                    attachment.contains(".pdf") -> stringResource(R.string.details_attach_pdf)
                                                    attachment.contains(".txt") -> stringResource(R.string.details_attach_text)
                                                    attachment.contains(".png|.jpg|.jpeg|.gif".toRegex()) ->
                                                        stringResource(R.string.details_attach_picture)
                                                    else -> stringResource(R.string.details_attach_other)
                                                }
                                                Text(text = fileType)
                                            }
                                        }
                                        Spacer(modifier = Modifier.size(8.dp))
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun Preview() {
    TaskManagerTheme {
        DetailsBody(
            DetailsBodyState(
                Task(
                    0,
                    System.currentTimeMillis().toString(),
                    System.currentTimeMillis().toString(),
                    "1 number number number number number number number number number number number number number",
                    "dddd ddd dd",
                    Task.Priority.NORMAL,
                    "In progress",
                    100500,
                    attachments = listOf("one.png", "two.pdf", "three.jpg", "other")
                ),
                listOf("status1", "status2")
            )
        ) {}
    }
}