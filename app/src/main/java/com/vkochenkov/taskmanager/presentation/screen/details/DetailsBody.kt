package com.vkochenkov.taskmanager.presentation.screen.details

import android.content.res.Configuration
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
import com.vkochenkov.taskmanager.utils.getPriorityColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsBody(
    state: DetailsBodyState,
    onAction: (DetailsActions) -> Unit
) {
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
                                // todo add click action
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
                    ShowContent(padding, state.task, onAction)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowContent(
    padding: PaddingValues,
    task: Task?,
    onAction: (DetailsActions) -> Unit
) {
    // todo improve UI

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        if (task != null) {
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
                    //todo to res
                    Text(
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                        text = stringResource(R.string.details_title_hint)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it })
            Spacer(modifier = Modifier.size(8.dp))
            OutlinedTextField(
                label = {
                    //todo to res
                    Text(
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                        text = stringResource(R.string.details_description_hint)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = { description = it })

        } else {
            // todo empty task
        }


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