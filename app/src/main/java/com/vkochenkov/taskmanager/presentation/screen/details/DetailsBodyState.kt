package com.vkochenkov.taskmanager.presentation.screen.details

import com.vkochenkov.taskmanager.data.model.Task

sealed class DetailsBodyState {

    data class ShowContent(
        val task: Task,
        val showDialogOnBack: Boolean = false
    ) : DetailsBodyState()

    object ShowEmpty : DetailsBodyState()

    object ShowError : DetailsBodyState()

    object ShowLoading: DetailsBodyState()
}
