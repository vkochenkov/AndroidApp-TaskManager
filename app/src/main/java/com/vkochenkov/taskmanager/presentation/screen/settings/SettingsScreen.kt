package com.vkochenkov.taskmanager.presentation.screen.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    navController: NavHostController
) {
    val viewModel: SettingsViewModel = koinViewModel()
    viewModel.navController = navController

    val bodyState by viewModel.state.collectAsState()

    SettingsBody(
        bodyState,
        viewModel.onAction
    )
}
