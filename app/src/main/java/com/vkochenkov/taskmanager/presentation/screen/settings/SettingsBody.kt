package com.vkochenkov.taskmanager.presentation.screen.settings

import android.content.res.Configuration
import android.view.MotionEvent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.vkochenkov.taskmanager.R
import com.vkochenkov.taskmanager.presentation.theme.TaskManagerTheme
import kotlin.math.roundToInt

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun SettingsBody(
    state: SettingsBodyState,
    onAction: (SettingsActions) -> Unit
) {

    var stats by remember {
        mutableStateOf(state.statuses)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = stringResource(R.string.settings_screen_title))
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onAction.invoke(SettingsActions.BackPressed)
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                )
            }
        ) { padding ->
            if (state.showNewStatusDialog) {
                if (state.statuses.size < 5) {
                    var statusStr by remember {
                        mutableStateOf("")
                    }
                    var isErrorSymbols by remember {
                        mutableStateOf(false)
                    }
                    var isErrorSameItem by remember {
                        mutableStateOf(false)
                    }
                    AlertDialog(
                        onDismissRequest = {
                            onAction.invoke(SettingsActions.CanselNewStatusDialog)
                        },
                        title = {
                            Text(text = stringResource(R.string.settings_new_status_dialog_title))
                        },
                        text = {
                            Column {
                                OutlinedTextField(
                                    isError = isErrorSymbols || isErrorSameItem,
                                    label = {
                                        if (isErrorSymbols) {
                                            Text(
                                                text = stringResource(R.string.error_wrong_number),
                                                color = MaterialTheme.colorScheme.error,
                                            )
                                        } else if (isErrorSameItem) {
                                            Text(
                                                text = stringResource(R.string.settings_new_status_same),
                                                color = MaterialTheme.colorScheme.error,
                                            )
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    value = statusStr,
                                    onValueChange = {
                                        isErrorSymbols = false
                                        isErrorSameItem = false
                                        statusStr = it
                                    }
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    onAction.invoke(SettingsActions.CanselNewStatusDialog)
                                }) {
                                Text(stringResource(R.string.settings_new_status_dialog_btn_dismiss))
                            }

                            Button(
                                onClick = {
                                    if (statusStr.length in 1..19) {
                                        isErrorSymbols = false
                                        if (!state.statuses.contains(statusStr)) {
                                            isErrorSameItem = false
                                            onAction.invoke(SettingsActions.AddNewStatus(statusStr))
                                        } else {
                                            isErrorSameItem = true
                                        }
                                    } else {
                                        isErrorSymbols = true
                                    }
                                }
                            ) {
                                Text(stringResource(R.string.settings_new_status_dialog_btn_confirm))
                            }
                        }
                    )
                } else {
                    AlertDialog(
                        onDismissRequest = {
                            onAction.invoke(SettingsActions.CanselNewStatusDialog)
                        },
                        title = {
                            Text(text = stringResource(R.string.settings_new_status_dialog_title))
                        },
                        text = {
                            Text(text = stringResource(R.string.settings_new_status_dialog_sorry_text))
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    onAction.invoke(SettingsActions.CanselNewStatusDialog)
                                }) {
                                Text(stringResource(R.string.settings_status_btn_ok))
                            }
                        }
                    )
                }
            }

            state.showCantDeleteStatusDialog?.let { reason ->
                AlertDialog(
                    onDismissRequest = {
                        onAction.invoke(SettingsActions.CanselCantDeleteStatusDialog)
                    },
                    title = {
                        Text(text = stringResource(R.string.settings_cant_delete_status_dialog_title))
                    },
                    text = {
                        Text(
                            text = stringResource(
                                when (reason) {
                                    SettingsBodyState.ReasonCantDeleteStatus.LAST -> R.string.settings_cant_delete_status_dialog_sorry_text_last
                                    SettingsBodyState.ReasonCantDeleteStatus.SAME -> R.string.settings_cant_delete_status_dialog_sorry_text_same
                                }
                            )
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                onAction.invoke(SettingsActions.CanselCantDeleteStatusDialog)
                            }) {
                            Text(stringResource(R.string.settings_status_btn_ok))
                        }
                    }
                )
            }

            state.renameStatusIndex?.let { index ->
                var statusStr by remember {
                    mutableStateOf(state.statuses[index])
                }
                var isErrorSymbols by remember {
                    mutableStateOf(false)
                }
                var isErrorSameItem by remember {
                    mutableStateOf(false)
                }
                AlertDialog(
                    onDismissRequest = {
                        onAction.invoke(SettingsActions.CanselNewStatusDialog)
                    },
                    title = {
                        Text(text = stringResource(R.string.settings_rename_status_dialog_title))
                    },
                    text = {
                        Column {
                            OutlinedTextField(
                                isError = isErrorSymbols || isErrorSameItem,
                                label = {
                                    if (isErrorSymbols) {
                                        Text(
                                            text = stringResource(R.string.error_wrong_number),
                                            color = MaterialTheme.colorScheme.error,
                                        )
                                    } else if (isErrorSameItem) {
                                        Text(
                                            text = stringResource(R.string.settings_new_status_same),
                                            color = MaterialTheme.colorScheme.error,
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                value = statusStr,
                                onValueChange = {
                                    isErrorSymbols = false
                                    isErrorSameItem = false
                                    statusStr = it
                                }
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                onAction.invoke(SettingsActions.CanselRenameStatusDialog)
                            }) {
                            Text(stringResource(R.string.settings_new_status_dialog_btn_dismiss))
                        }

                        Button(
                            onClick = {
                                if (statusStr.length in 1..19) {
                                    isErrorSymbols = false
                                    if (!state.statuses.contains(statusStr)) {
                                        isErrorSameItem = false
                                        onAction.invoke(
                                            SettingsActions.RenameStatus(
                                                statusStr,
                                                index
                                            )
                                        )
                                    } else {
                                        isErrorSameItem = true
                                    }
                                } else {
                                    isErrorSymbols = true
                                }
                            }
                        ) {
                            Text(stringResource(R.string.settings_rename_status_dialog_btn_confirm))
                        }
                    }
                )
            }

            Column(
                modifier = Modifier.padding(paddingValues = padding)

            ) {
                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                    item {
                        Divider()
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(R.string.setting_screen_statuses))
                            IconButton(onClick = {
                                onAction.invoke(SettingsActions.ShowNewStatusDialog)
                            }) {
                                Icon(Icons.Default.Add, contentDescription = null)
                            }
                        }

                        stats.forEachIndexed { index, status ->

                            var offsetY by remember { mutableStateOf(0f) }

                            var wasDragged by remember { mutableStateOf(false) }

                            var columnHeightPx by remember {
                                mutableStateOf(0f)
                            }

                            AssistChip(
                                modifier = Modifier
                                    .onGloballyPositioned { coordinates ->
                                        columnHeightPx = coordinates.size.height.toFloat()
                                    }
                                    .offset { IntOffset(0, offsetY.roundToInt()) }
                                    .pointerInput(Unit) {
                                        detectDragGestures(
                                            onDragEnd = {
                                                offsetY = 0f
                                                wasDragged = false
                                            },
                                            onDrag = { change, dragAmount ->
                                                if (change.pressed) {
                                                    if (!wasDragged) {
                                                        offsetY += dragAmount.y
                                                        if (offsetY > columnHeightPx) {
                                                            try {
                                                                val newStats = stats.toMutableList()
                                                                val cur = newStats.get(index)
                                                                newStats.removeAt(index)
                                                                newStats.add(index + 1, cur)
                                                                stats = newStats
                                                            } catch (ex: Exception) {
                                                                // do nothing
                                                            } finally {
                                                                offsetY = 0f
                                                                wasDragged = true
                                                            }
                                                        } else if (offsetY < -columnHeightPx) {
                                                            try {
                                                                val newStats = stats.toMutableList()
                                                                val cur = newStats.get(index)
                                                                newStats.removeAt(index)
                                                                newStats.add(index - 1, cur)
                                                                stats = newStats
                                                            } catch (ex: Exception) {
                                                                // do nothing
                                                            } finally {
                                                                offsetY = 0f
                                                                wasDragged = true
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        )
                                    },
                                onClick = {
                                    onAction.invoke(SettingsActions.ShowRenameStatusDialog(index))
                                },
                                label = { Text(status) },
                                trailingIcon = {
                                    if (state.loadingStatusIndex == index) {
                                        IconButton(onClick = {
                                            // do nothing
                                        }) {
                                            CircularProgressIndicator(modifier = Modifier.size(25.dp))
                                        }
                                    } else {
                                        IconButton(onClick = {
                                            onAction.invoke(SettingsActions.DeleteStatus(index))
                                        }) {
                                            Icon(Icons.Default.Delete, contentDescription = null)
                                        }
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                        }
                        Divider()
                    }
                }
            }
            // Block interaction when loading
            if (state.loadingStatusIndex != null) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null, // Disable ripple effect
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                // Do nothing
                            }
                        ),
                )
            }
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun Preview() {
    TaskManagerTheme {
        SettingsBody(
            state = SettingsBodyState(listOf("status1", "status2"))
        ) {}
    }
}
