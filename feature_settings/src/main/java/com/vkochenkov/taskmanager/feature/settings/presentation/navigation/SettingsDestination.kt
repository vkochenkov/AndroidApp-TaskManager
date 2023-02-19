package com.vkochenkov.taskmanager.feature.settings.presentation.navigation

import com.vkochenkov.taskmanager.base.presentation.navigation.Route

sealed class SettingsDestination : Route {

    object Settings : SettingsDestination() {

        override val route: String = "SettingsScreen"
    }
}

