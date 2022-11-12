package com.vkochenkov.taskmanager.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vkochenkov.taskmanager.presentation.screen.details.DetailsScreen
import com.vkochenkov.taskmanager.presentation.screen.main.MainScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    // todo move to enum
    startDestination: String = "main"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("main") { MainScreen(navController) }
        composable("details") { DetailsScreen(navController) }
    }
}