package com.vkochenkov.taskmanager.presentation.screen.details

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DetailsScreen() {

    val viewModel: DetailsViewModel = hiltViewModel()

    DetailsBody(
        viewModel.state.value,
        viewModel.onAction
    )
}
