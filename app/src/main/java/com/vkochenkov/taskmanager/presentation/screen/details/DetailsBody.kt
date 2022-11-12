package com.vkochenkov.taskmanager.presentation.screen.details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vkochenkov.taskmanager.presentation.theme.TaskManagerTheme

@Composable
fun DetailsBody(
    state: DetailsBodyState,
    onAction: (DetailsActions) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Scaffold {
            Button(onClick = {
                onAction.invoke(DetailsActions.OnClick)
            }) {
                if (state is DetailsBodyState.ShowData) {
                    Text(text = "New screen")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    TaskManagerTheme {
        DetailsBody(
            DetailsBodyState.ShowData("fffff")
        ) {}
    }
}