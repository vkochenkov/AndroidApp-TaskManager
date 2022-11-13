package com.vkochenkov.taskmanager.presentation.screen.details

import com.vkochenkov.taskmanager.data.model.Task

sealed class DetailsBodyState {

    data class ShowContent(
        var task: Task?
    ): DetailsBodyState()
}
