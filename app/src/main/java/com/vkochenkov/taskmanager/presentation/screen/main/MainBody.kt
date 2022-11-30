package com.vkochenkov.taskmanager.presentation.screen.main

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vkochenkov.taskmanager.R
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.theme.TaskManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBody(
    state: MainBodyState,
    onAction: (MainActions) -> Unit
) {
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
                is MainBodyState.ShowContent -> ShowContent(padding, state.tasksList, onAction)
                is MainBodyState.ShowEmpty -> ErrorState(
                    padding = padding,
                    text = stringResource(id = R.string.main_empty_text)
                )
                is MainBodyState.ShowError -> ErrorState(padding)
                is MainBodyState.ShowLoading -> LoadingState(padding)
            }
        }
    }
}

@Composable
private fun ShowContent(
    padding: PaddingValues,
    tasksList: List<Task>,
    onAction: (MainActions) -> Unit
) {
    // todo fix incorrect position after go back on this screen

    Column {
        LazyRow {
            for (status in Task.Status.values().filter { it != Task.Status.DONE }) {
                item {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(250.dp)
                            .padding(padding),
                        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        val filtered = tasksList.filter { it.status == status }
                        item {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = status.getNameForUi(LocalContext.current),
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Cursive
                            )
                        }
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
    }
}

@Composable
private fun ErrorState(
    padding: PaddingValues,
    text: String = stringResource(R.string.main_error_text)
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 18.sp,
            text = text,
            textAlign = TextAlign.Center
        )
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
    val minClickableCardSize = 48.dp
    val cardHeight = remember { mutableStateOf(minClickableCardSize) }
    val localDensity = LocalDensity.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = minClickableCardSize),
        onClick = {
            onAction.invoke(MainActions.OpenDetails(task.id))
        }
    ) {
        Box {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .width(16.dp)
                        .height(cardHeight.value)
                        .background(
                            color = task.priority.getColor(), shape = RoundedCornerShape(
                                topStart = 0.dp,
                                bottomStart = 0.dp
                            )
                        )
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 10.dp, end = 16.dp)
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        val newSize = with(localDensity) { it.size.height.toDp() }
                        cardHeight.value =
                            if (newSize > minClickableCardSize) newSize else minClickableCardSize
                    },
            ) {
                Spacer(modifier = Modifier.size(8.dp))
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
fun PreviewFull() {
    TaskManagerTheme {
        MainBody(
            MainBodyState.ShowContent(
                listOf(
                    Task(
                        0,
                        "1 number number number number number number number number number number number number number",
                        "dddd ddd dd",
                        Task.Priority.NORMAL,
                        Task.Status.IN_PROGRESS
                    ),
                    Task(
                        1,
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun PreviewEmpty() {
    TaskManagerTheme {
        MainBody(
            MainBodyState.ShowContent(
                listOf()
            )
        ) {}
    }
}