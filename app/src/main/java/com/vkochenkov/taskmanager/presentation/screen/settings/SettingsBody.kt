package com.vkochenkov.taskmanager.presentation.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBody(
    state: SettingsBodyState,
    onAction: (SettingsActions) -> Unit
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
                                onAction.invoke(SettingsActions.BackPressed())
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
            when (state) {
                is SettingsBodyState.Content -> ContentState(
                    padding,
                    onAction
                )
            }
        }
    }
}

@Composable
fun ContentState(padding: PaddingValues, onAction: (SettingsActions) -> Unit) {
    Column(
        modifier = Modifier.padding(paddingValues = padding)
    ) {
        Text(text = "todo")
        // todo vlad
    }
}

//
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
//@Composable
//fun PreviewEmpty() {
//    TaskManagerTheme {
//        SettingsBody(
//            SettingsBodyState.Content(
//                listOf(),
//                listOf()
//            )
//        ) {}
//    }
//}
