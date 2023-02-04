package com.vkochenkov.taskmanager.presentation.screen.details

import com.vkochenkov.taskmanager.data.model.Task

sealed class DetailsBodyState {

    data class Content(
        val task: Task,
        val statuses: List<String>,
        val showOnBackDialog: Boolean = false,
        val showOnDeleteDialog: Boolean = false,
        val showNotificationDialog: Boolean = false,
        val showTitleValidation: Boolean = false,
        val showDescriptionValidation: Boolean = false
    ) : DetailsBodyState()

    object Error : DetailsBodyState()

    object Loading : DetailsBodyState()
}
