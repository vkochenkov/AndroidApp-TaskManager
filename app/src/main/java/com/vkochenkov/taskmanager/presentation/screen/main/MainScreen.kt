package com.vkochenkov.taskmanager.presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    navController: NavHostController
) {

    val viewModel: MainViewModel = koinViewModel()
    viewModel.navController = navController

    LaunchedEffect(navController) {
        viewModel.onAction.invoke(MainActions.UpdateData)
    }

    MainBody(
        viewModel.state.value,
        viewModel.onAction
    )
}
