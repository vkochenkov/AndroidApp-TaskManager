package com.vkochenkov.taskmanager.presentation.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
    LazyColumn(modifier = Modifier.padding(padding)) {
        for (task in tasksList) {
            item {
                TaskCard(task, onAction)
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onAction: (MainActions) -> Unit
) {
    Card(
        modifier = Modifier.clickable {
            onAction.invoke(MainActions.OpenDetails(task.id))
        }
    ) {
        Text(text = task.title)
        task.description?.let {
            Text(text = it)
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

@Preview(showBackground = true)
@Composable
fun Preview() {
    TaskManagerTheme {
        // todo
    }
}