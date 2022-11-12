package com.vkochenkov.taskmanager.presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun MainScreen(
    navController: NavHostController
) {

    val viewModel: MainViewModel = viewModel()
    viewModel.prepareViewModel(navController)

    MainBody(
        viewModel.state.value,
        viewModel.onAction
    )
}
