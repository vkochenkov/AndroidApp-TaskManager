package com.vkochenkov.taskmanager.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vkochenkov.taskmanager.base.presentation.navigation.CoreDestination
import com.vkochenkov.taskmanager.feature.main.presentation.navigation.mainNavigation
import com.vkochenkov.taskmanager.feature.settings.presentation.navigation.settingsNavDestination

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = CoreDestination.Main.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        mainNavigation(this, navController)
        settingsNavDestination(this, navController)
    }
}