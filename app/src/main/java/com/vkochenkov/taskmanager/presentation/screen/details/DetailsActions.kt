package com.vkochenkov.taskmanager.presentation.screen.details

import com.vkochenkov.taskmanager.data.model.Task

sealed class DetailsActions {

    data class TaskChanged(val task: Task) : DetailsActions()

    data class BackPressed(val showDialog: Boolean? = null) : DetailsActions()

    object SaveTask : DetailsActions()

    data class DeleteTask(val showDialog: Boolean? = null) : DetailsActions()

    object CancelBackDialog : DetailsActions()

    object CancelDeleteDialog : DetailsActions()

}
