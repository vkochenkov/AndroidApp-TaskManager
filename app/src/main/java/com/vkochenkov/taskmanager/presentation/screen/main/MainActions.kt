package com.vkochenkov.taskmanager.presentation.screen.main

sealed class MainActions {

    data class OnOpenDetails(val id: String) : MainActions()

    object OnAddNewTask : MainActions()

}
