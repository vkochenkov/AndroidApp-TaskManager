package com.vkochenkov.taskmanager.presentation.screen.details

import com.vkochenkov.taskmanager.data.model.Task

data class DetailsBodyState(
    val task: Task?,
    val statuses: List<String>,
    val showOnBackDialog: Boolean = false,
    val showOnDeleteDialog: Boolean = false,
    val showNotificationDialog: Boolean = false,
    val showTitleValidation: Boolean = false,
    val showDescriptionValidation: Boolean = false,
    val isLoadingPage: Boolean = false,
    val isErrorPage: Boolean = false
)

