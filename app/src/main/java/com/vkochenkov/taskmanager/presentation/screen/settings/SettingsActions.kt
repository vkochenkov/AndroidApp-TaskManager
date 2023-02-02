package com.vkochenkov.taskmanager.presentation.screen.settings


sealed class SettingsActions {

    data class BackPressed(val showDialog: Boolean? = null) : SettingsActions()

//    data class OpenDetails(val id: Int) : SettingsActions()
//
//    object AddNewTask : SettingsActions()
//
//    object UpdateData : SettingsActions()
}
