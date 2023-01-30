package com.vkochenkov.taskmanager.presentation.screen.main

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.vkochenkov.taskmanager.R
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.components.ErrorState
import com.vkochenkov.taskmanager.presentation.theme.TaskManagerTheme
import com.vkochenkov.taskmanager.presentation.utils.getColor
import com.vkochenkov.taskmanager.presentation.utils.getNameForUi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun MainBody(
    state: MainBodyState,
    onAction: (MainActions) -> Unit
) {
    val pagerState: PagerState = rememberPagerState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        onAction.invoke(MainActions.AddNewTask)
                    }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.main_btn_add_task)
                    )
                }
            }
        ) { padding ->
            when (state) {
                is MainBodyState.Content -> ContentState(
                    padding,
                    state.tasksList,
                    pagerState,
                    onAction
                )
                is MainBodyState.Empty -> ErrorState(
                    padding = padding,
                    text = stringResource(id = R.string.main_empty_text)
                )
                is MainBodyState.Error -> ErrorState(padding)
                is MainBodyState.Loading -> LoadingState(padding)
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ContentState(
    padding: PaddingValues,
    tasksList: List<Task>,
    pagerState: PagerState,
    onAction: (MainActions) -> Unit
) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(pagerState.currentPage) }
    val coroutineScope = rememberCoroutineScope()

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex
        ) {
            Task.Status.values().forEachIndexed { index, status ->
                Tab(
                    selected = false,
                    onClick = {
                        selectedTabIndex = index
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(selectedTabIndex)
                        }
                    }
                ) {
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(text = status.getNameForUi())
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
        }
        HorizontalPager(
            state = pagerState,
            count = Task.Status.values().size
        ) { pageIndex ->
            selectedTabIndex = pagerState.currentPage
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(padding),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val filtered = tasksList.filter { it.status == Task.Status.values()[pageIndex] }
                if (filtered.isNotEmpty()) {
                    for (task in filtered) {
                        item {
                            TaskCard(task, onAction)
                        }
                    }
                } else {
                    item {
                        ErrorState(
                            padding = padding,
                            text = stringResource(id = R.string.main_empty_text_status)
                        )
                    }
                }
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskCard(
    task: Task,
    onAction: (MainActions) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp),
        onClick = {
            onAction.invoke(MainActions.OpenDetails(task.id))
        },
        colors = CardDefaults.cardColors(task.priority.getColor())
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = task.title,
            fontSize = 18.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun PreviewFull() {
    TaskManagerTheme {
        MainBody(
            MainBodyState.Content(
                listOf(
                    Task(
                        id = 0,
                        creationDate = System.currentTimeMillis().toString(),
                        updateDate = System.currentTimeMillis().toString(),
                        title = "1 number number number number number number number number number number number number number",
                        description = "dddd ddd dd",
                        priority = Task.Priority.NORMAL,
                        status = Task.Status.IN_PROGRESS,
                        notificationTime = null
                    ),
                    Task(
                        1,
                        System.currentTimeMillis().toString(),
                        System.currentTimeMillis().toString(),
                        "2 number number",
                        "dddde rere dd",
                        Task.Priority.LOW,
                        Task.Status.IN_PROGRESS,
                        notificationTime = 100500
                    )
                )
            )
        ) {}
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun PreviewEmpty() {
    TaskManagerTheme {
        MainBody(
            MainBodyState.Content(
                listOf()
            )
        ) {}
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun PreviewTaskCard() {
    TaskManagerTheme {
        TaskCard(
            task = Task(
                id = 0,
                creationDate = System.currentTimeMillis().toString(),
                updateDate = System.currentTimeMillis().toString(),
                title = "1 number number number number number number number number number number number number number",
                description = "dddd ddd dd",
                priority = Task.Priority.NORMAL,
                status = Task.Status.IN_PROGRESS,
                notificationTime = null
            )
        ) {}
    }
}