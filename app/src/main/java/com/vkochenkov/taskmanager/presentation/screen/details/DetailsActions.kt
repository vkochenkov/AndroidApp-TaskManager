package com.vkochenkov.taskmanager.presentation.screen.details

import com.vkochenkov.taskmanager.data.model.Task

sealed class DetailsActions {

    data class OnTaskChanged(val task: Task): DetailsActions()

    object OnNavigateBack: DetailsActions()
}
