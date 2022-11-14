package com.vkochenkov.taskmanager.presentation.screen.main

sealed class MainActions {

    data class OpenDetails(val id: String) : MainActions()

    object AddNewTask : MainActions()

}
