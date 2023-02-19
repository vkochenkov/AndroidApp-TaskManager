package com.vkochenkov.taskmanager.feature.main.presentation.screen.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    navController: NavHostController
) {
    val viewModel: DetailsViewModel = koinViewModel()
    viewModel.navController = navController

    val bodyState by viewModel.state.collectAsState()

    DetailsBody(
        bodyState,
        viewModel.onAction
    )
}
