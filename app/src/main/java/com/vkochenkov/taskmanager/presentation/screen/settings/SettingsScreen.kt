package com.vkochenkov.taskmanager.presentation.screen.settings

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    navController: NavHostController
) {
    val viewModel: SettingsViewModel = koinViewModel()
    viewModel.navController = navController

    SettingsBody(
        viewModel.state.value,
        viewModel.onAction
    )
}
