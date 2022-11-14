package com.vkochenkov.taskmanager.presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MainScreen(
    navController: NavHostController
) {

    val viewModel: MainViewModel = koinViewModel()
    viewModel.navController = navController

    MainBody(
        viewModel.state.value,
        viewModel.onAction
    )
}
