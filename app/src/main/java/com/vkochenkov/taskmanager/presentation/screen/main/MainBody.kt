package com.vkochenkov.taskmanager.presentation.screen.main

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.theme.TaskManagerTheme
import com.vkochenkov.taskmanager.utils.getPriorityColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBody(
    state: MainBodyState,
    onAction: (MainActions) -> Unit
) {
    // todo use pager?
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold { padding ->
            when (state) {
                is MainBodyState.ShowContent -> ShowContent(padding, state.tasksList, onAction)
                else -> ErrorState(padding)
            }
        }
    }
}

@Composable
private fun ShowContent(
    padding: PaddingValues,
    tasksList: List<Task>?,
    onAction: (MainActions) -> Unit
) {
    if (!tasksList.isNullOrEmpty()) {
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.size(8.dp))
            }
            for (task in tasksList) {
                item {
                    TaskCard(task, onAction)
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
        }
    } else {
        Text(text = "no content")
    }
}

@Composable
private fun ErrorState(
    padding: PaddingValues
) {
    Column() {
        Text(text = "error")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskCard(
    task: Task,
    onAction: (MainActions) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            onAction.invoke(MainActions.OpenDetails(task.id))
        }
    ) {
        Box(modifier = Modifier.fillMaxHeight()) {
            //todo move to side without top
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
                    .background(
                        color = getPriorityColor(task.priority), shape = RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = task.title,
                    fontSize = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun Preview() {
    TaskManagerTheme {
        MainBody(
            MainBodyState.ShowContent(
                listOf(
                    Task(
                        "1",
                        "1 number number number number number number number number number number number number number",
                        "dddd ddd dd",
                        Task.Priority.NORMAL,
                        Task.Status.IN_PROGRESS
                    ),
                    Task(
                        "2",
                        "2 number number",
                        "dddde rere dd",
                        Task.Priority.LOW,
                        Task.Status.IN_PROGRESS
                    )
                )
            )
        ) {}
    }
}