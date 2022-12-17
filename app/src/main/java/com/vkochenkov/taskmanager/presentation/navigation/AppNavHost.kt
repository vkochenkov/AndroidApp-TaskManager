package com.vkochenkov.taskmanager.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vkochenkov.taskmanager.presentation.screen.details.DetailsScreen
import com.vkochenkov.taskmanager.presentation.screen.main.MainScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destination.Main.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Destination.Main.route) { MainScreen(navController) }
        composable(
            route = Destination.Details.routeWithArgs,
            arguments = listOf(navArgument(Destination.Details.argument1) {
                type = NavType.StringType
                nullable = true
            })
        ) {
            DetailsScreen(navController)
        }
    }
}