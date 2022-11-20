package com.vkochenkov.taskmanager.presentation.screen.main

sealed class MainActions {

    data class OpenDetails(val id: Int) : MainActions()

    object AddNewTask : MainActions()

    object UpdateData : MainActions()
}
