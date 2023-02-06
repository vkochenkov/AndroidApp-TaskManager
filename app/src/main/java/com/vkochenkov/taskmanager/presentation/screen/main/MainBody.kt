package com.vkochenkov.taskmanager.presentation.screen.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.vkochenkov.taskmanager.presentation.components.CustomScaffold
import com.vkochenkov.taskmanager.presentation.components.ErrorState
import com.vkochenkov.taskmanager.presentation.theme.TaskManagerTheme
import com.vkochenkov.taskmanager.presentation.utils.getColor
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
        CustomScaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.main_screen_title))
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onAction.invoke(MainActions.Exit)
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = null
                                )
                            }
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                onAction.invoke(MainActions.AddNewTask)
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.main_btn_add_task)
                                )
                            }
                        )
                        IconButton(
                            onClick = {
                                onAction.invoke(MainActions.OpenSettings)
                            },
                            content = {
                                Icon(
                                    painterResource(id = R.drawable.ic_baseline_settings_24),
                                    contentDescription = stringResource(R.string.main_btn_settings)
                                )
                            }
                        )
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onClick = {
                        onAction.invoke(MainActions.AddNewTask)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.main_btn_add_task)
                    )
                }
            },
            isLoadingPage = state.isLoadingPage,
            isErrorPage = state.isErrorPage
        ) { padding ->

            if (state.tasksList.isEmpty() || state.statusesList.isEmpty()) {
                ErrorState(
                    Modifier.padding(padding),
                    text = stringResource(id = R.string.main_empty_text)
                )
            } else {

                var selectedTabIndex by rememberSaveable(state.statusesList) {
                    if (pagerState.currentPage < state.statusesList.size) {
                        mutableStateOf(pagerState.currentPage)
                    } else {
                        mutableStateOf(0)
                    }
                }
                val coroutineScope = rememberCoroutineScope()

                Column(
                    modifier = Modifier.padding(padding)
                ) {
                    Divider()
                    TabRow(
                        selectedTabIndex = selectedTabIndex
                    ) {
                        state.statusesList.forEachIndexed { index, status ->
                            Tab(
                                selected = false,
                                onClick = {
                                    selectedTabIndex = index
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(selectedTabIndex)
                                    }
                                }
                            ) {
                                Spacer(modifier = Modifier.size(12.dp))
                                Text(text = status)
                                Spacer(modifier = Modifier.size(12.dp))
                            }
                        }
                    }
                    HorizontalPager(
                        state = pagerState,
                        count = state.statusesList.size
                    ) { pageIndex ->
                        selectedTabIndex = pagerState.currentPage
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxHeight(),
                            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            val filtered =
                                state.tasksList.filter { it.status == state.statusesList[pageIndex] }
                            if (filtered.isNotEmpty()) {
                                for (task in filtered) {
                                    item {
                                        TaskCard(task, onAction)
                                    }
                                }
                            } else {
                                item {
                                    ErrorState(
                                        text = stringResource(id = R.string.main_empty_text_status)
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
            MainBodyState(
                listOf(
                    Task(
                        id = 0,
                        creationDate = System.currentTimeMillis().toString(),
                        updateDate = System.currentTimeMillis().toString(),
                        title = "1 number number number number number number number number number number number number number",
                        description = "dddd ddd dd",
                        priority = Task.Priority.NORMAL,
                        status = "In progress",
                        notificationTime = null
                    ),
                    Task(
                        1,
                        System.currentTimeMillis().toString(),
                        System.currentTimeMillis().toString(),
                        "2 number number",
                        "dddde rere dd",
                        Task.Priority.LOW,
                        "In progress",
                        notificationTime = 100500
                    )
                ),
                listOf("status1", "status2")
            )
        ) {}
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun PreviewEmpty() {
    TaskManagerTheme {
        MainBody(
            MainBodyState(
                listOf(),
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
                status = "In progress",
                notificationTime = null
            )
        ) {}
    }
}