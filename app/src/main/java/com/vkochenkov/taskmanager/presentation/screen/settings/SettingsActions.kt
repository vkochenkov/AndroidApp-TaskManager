package com.vkochenkov.taskmanager.presentation.screen.settings


sealed class SettingsActions {

    object BackPressed : SettingsActions()

    object ShowNewStatusDialog : SettingsActions()

    object CanselNewStatusDialog : SettingsActions()

    object CanselCantDeleteStatusDialog : SettingsActions()

    data class AddNewStatus(
        val status: String
    ) : SettingsActions()

    data class DeleteStatus(
        val index: Int
    ) : SettingsActions()
}
