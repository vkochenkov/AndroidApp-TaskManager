package com.vkochenkov.taskmanager.feature.settings.presentation.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.vkochenkov.taskmanager.base.presentation.navigation.CoreDestination
import com.vkochenkov.taskmanager.feature.settings.presentation.screen.settings.SettingsScreen

fun settingsNavDestination(
    navGraphBuilder: NavGraphBuilder,
    navController: NavHostController
) {
    navGraphBuilder.navigation(
        route = CoreDestination.Settings.route,
        startDestination = SettingsDestination.Settings.route
    ) {
        composable(
            SettingsDestination.Settings.route
        ) {
            SettingsScreen(navController)
        }
    }
}
