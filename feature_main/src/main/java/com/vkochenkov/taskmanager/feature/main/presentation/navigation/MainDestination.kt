package com.vkochenkov.taskmanager.feature.main.presentation.navigation

import com.vkochenkov.taskmanager.base.presentation.navigation.Route
import com.vkochenkov.taskmanager.base.presentation.navigation.RouteWith1Arg

sealed class MainDestination : Route {

    object Main : MainDestination() {

        override val route: String = "MainScreen"
    }

    object Details : MainDestination(), RouteWith1Arg {

        override val argument1: String = "id"

        override val route: String = "DetailsScreen"
    }
}

