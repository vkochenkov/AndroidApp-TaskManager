package com.vkochenkov.taskmanager.presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    val bodyState by viewModel.state.collectAsState()

    MainBody(
        bodyState,
        viewModel.onAction
    )
}
