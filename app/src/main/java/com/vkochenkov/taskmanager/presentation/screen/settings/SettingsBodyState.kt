package com.vkochenkov.taskmanager.presentation.screen.settings

data class SettingsBodyState(
    val statuses: List<String>,
    val showNewStatusDialog: Boolean = false,
    val showCantDeleteStatusDialog: ReasonCantDeleteStatus? = null,
    val loadingStatusIndex: Int? = null,
    val renameStatusIndex: Int? = null
) {

    enum class ReasonCantDeleteStatus {
        LAST,
        SAME
    }
}


