package com.vkochenkov.taskmanager.presentation.screen.main

sealed class MainActions {

    data class OnOpenDetails(val id: Int) : MainActions()

    object OnAddNewTask : MainActions()

}
