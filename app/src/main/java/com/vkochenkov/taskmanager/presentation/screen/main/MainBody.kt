package com.vkochenkov.taskmanager.presentation.screen.main

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.theme.TaskManagerTheme

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
                is MainBodyState.HasContent -> HasContentState(padding, state.tasksList, onAction)
                else -> EmptyContentState(padding)
            }
        }
    }
}

@Composable
fun HasContentState(
    padding: PaddingValues,
    tasksList: List<Task>,
    onAction: (MainActions) -> Unit
) {
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
}

@Composable
fun EmptyContentState(
    padding: PaddingValues
) {
    Column() {
        Text(text = "empty content")
    }
}

@Composable
fun TaskCard(
    task: Task,
    onAction: (MainActions) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onAction.invoke(MainActions.OpenDetails(task.id))
            }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val color = when (task.priority) {
                    Task.Priority.LOW -> {
                        Color.Green
                    }
                    Task.Priority.NORMAL -> {
                        Color.Yellow
                    }
                    Task.Priority.HIGH -> {
                        Color.Red
                    }
                }
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .border(border = BorderStroke(1.dp, Color.Black), shape = CircleShape)
                        .background(color = color, shape = CircleShape)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = task.priority.toString(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp
                )
            }
            Text(
                text = task.title,
                fontSize = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun Preview() {
    TaskManagerTheme {
        MainBody(
            MainBodyState.HasContent(
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