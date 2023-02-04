package com.vkochenkov.taskmanager.presentation.screen.settings


sealed class SettingsActions {

    object BackPressed : SettingsActions()

    object ShowNewStatusDialog : SettingsActions()

    data class ShowRenameStatusDialog(val index: Int) : SettingsActions()

    object CanselNewStatusDialog : SettingsActions()

    object CanselRenameStatusDialog : SettingsActions()

    object CanselCantDeleteStatusDialog : SettingsActions()

    data class AddNewStatus(
        val status: String
    ) : SettingsActions()

    data class DeleteStatus(
        val index: Int
    ) : SettingsActions()

    data class RenameStatus(val status: String, val index: Int) : SettingsActions()
}
