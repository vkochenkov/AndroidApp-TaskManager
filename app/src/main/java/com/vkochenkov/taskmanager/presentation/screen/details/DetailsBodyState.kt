package com.vkochenkov.taskmanager.presentation.screen.details

import com.vkochenkov.taskmanager.data.model.Task

sealed class DetailsBodyState {

    data class Content(
        val task: Task,
        val validationText: String? = null,
        val showDialogOnBack: Boolean = false,
        val showDialogOnDelete: Boolean = false,
        val showTitleValidation: Boolean = false,
        val showDescriptionValidation: Boolean = false
    ) : DetailsBodyState()

    object Error : DetailsBodyState()

    object Loading : DetailsBodyState()
}
