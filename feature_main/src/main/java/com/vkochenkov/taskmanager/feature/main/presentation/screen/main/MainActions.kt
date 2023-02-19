package com.vkochenkov.taskmanager.feature.main.presentation.screen.main

sealed class MainActions {

    data class OpenDetails(val id: Int) : MainActions()

    object AddNewTask : MainActions()

    object UpdateData : MainActions()

    object OpenSettings : MainActions()

    object Exit : MainActions()
}
