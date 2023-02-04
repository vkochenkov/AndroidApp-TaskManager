package com.vkochenkov.taskmanager.presentation.screen.settings


sealed class SettingsBodyState {

    data class Content(
        val statuses: List<String>,
        val showNewStatusDialog: Boolean = false,
        val showCantDeleteStatusDialog: ReasonCantDeleteStatus? = null,
        val loadingStatusIndex: Int? = null,
        val renameStatusIndex: Int? = null
    ) : SettingsBodyState() {

        enum class ReasonCantDeleteStatus {
            LAST,
            SAME
        }
    }
}


