package com.vkochenkov.taskmanager.presentation.screen.details

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    navController: NavHostController
) {

    val viewModel: DetailsViewModel = koinViewModel()
    viewModel.navController = navController

    DetailsBody(
        viewModel.state.value,
        viewModel.onAction
    )
}
