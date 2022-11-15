package com.vkochenkov.taskmanager.presentation.screen.details

import com.vkochenkov.taskmanager.data.model.Task

sealed class DetailsActions {

    data class OnTaskChanged(val task: Task) : DetailsActions()

    data class OnBackPressed(val showDialog: Boolean? = null) : DetailsActions()

    object OnSaveTask : DetailsActions()

    object OnCancelDialog : DetailsActions()

}
