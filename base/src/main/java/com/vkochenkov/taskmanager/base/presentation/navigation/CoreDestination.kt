package com.vkochenkov.taskmanager.base.presentation.navigation

sealed class CoreDestination : Route {

    object Main : CoreDestination() {

        override val route: String = "Main"
    }

    object Settings: CoreDestination() {

        override val route: String = "Settings"
    }
}