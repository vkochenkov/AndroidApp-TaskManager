package com.vkochenkov.taskmanager.feature.main.presentation.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.vkochenkov.taskmanager.base.presentation.navigation.CoreDestination
import com.vkochenkov.taskmanager.base.presentation.navigation.Route.Companion.buildDeeplink
import com.vkochenkov.taskmanager.feature.main.presentation.screen.details.DetailsScreen
import com.vkochenkov.taskmanager.feature.main.presentation.screen.main.MainScreen

fun mainNavigation(
    navGraphBuilder: NavGraphBuilder,
    navController: NavHostController
) {
    navGraphBuilder.navigation(
        route = CoreDestination.Main.route,
        startDestination = MainDestination.Main.route
    ) {
        composable(
            MainDestination.Main.route
        ) {
            MainScreen(navController)
        }
        composable(
            route = MainDestination.Details.routeWithArgs,
            arguments = listOf(navArgument(MainDestination.Details.argument1) {
                type = NavType.StringType
                nullable = true
            }),
            deepLinks = listOf(navDeepLink {
                uriPattern = buildDeeplink(MainDestination.Details.routeWithArgs)
            })
        ) {
            DetailsScreen(navController)
        }
    }
}
