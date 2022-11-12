package com.vkochenkov.taskmanager.presentation.screen.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vkochenkov.taskmanager.presentation.theme.TaskManagerTheme

@Composable
fun MainBody(
    state: MainBodyState,
    onAction: (MainActions) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Scaffold {
            Button(onClick = {
                onAction.invoke(MainActions.OpenDetails)
            }) {
                if (state is MainBodyState.ShowData) {
                    Text(text = "open details and state value is = ${state.title}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    TaskManagerTheme {
        MainBody(
            MainBodyState.ShowData("fffff")
        ) {}
    }
}