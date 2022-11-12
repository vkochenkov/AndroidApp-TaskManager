package com.vkochenkov.taskmanager.presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainScreen() {

    val viewModel: MainViewModel = hiltViewModel()

    MainBody(
        viewModel.state.value,
        viewModel.onAction
    )
}
