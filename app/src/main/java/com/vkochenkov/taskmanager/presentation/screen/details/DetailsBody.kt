package com.vkochenkov.taskmanager.presentation.screen.details

import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vkochenkov.taskmanager.R
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.theme.TaskManagerTheme
import com.vkochenkov.taskmanager.presentation.utils.getPriorityColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsBody(
    state: DetailsBodyState,
    onAction: (DetailsActions) -> Unit
) {

    val onTaskChanged = { task: Task ->
        onAction.invoke(DetailsActions.OnTaskChanged(task))
    }

    var onHandleBack by remember {
        mutableStateOf(true)
    }

    BackHandler(enabled = onHandleBack, onBack = {
        Log.d("vladd", "back")
        onHandleBack = false
        onAction.invoke(DetailsActions.OnNavigateBack)
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
                                onAction.invoke(DetailsActions.OnNavigateBack)
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
                                // todo add click action
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                )
            }
        ) { padding ->
            when (state) {
                is DetailsBodyState.ShowContent -> {
                    ShowContent(padding, state.task, state.isShowSaveDialog, onTaskChanged, onAction)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowContent(
    padding: PaddingValues,
    task: Task,
    isShowSaveDialod: Boolean,
    onTaskChanged: (Task) -> Unit,
    onAction: (DetailsActions) -> Unit
) {
    // todo improve UI

    // todo
    if (isShowSaveDialod) {
        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            confirmButton = { }
        )
    }

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        var title by remember { mutableStateOf(task.title) }
        var description by remember { mutableStateOf(task.description ?: "") }
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                    .background(color = getPriorityColor(task.priority), shape = CircleShape)
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
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
                onTaskChanged.invoke(task.copy(title = title))
            })
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
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
                onTaskChanged.invoke(task.copy(description = it))
            })
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun Preview() {
    TaskManagerTheme {
        DetailsBody(
            DetailsBodyState.ShowContent(
                Task(
                    "1",
                    "1 number number number number number number number number number number number number number",
                    "dddd ddd dd",
                    Task.Priority.NORMAL,
                    Task.Status.IN_PROGRESS
                )
            )
        ) {}
    }
}