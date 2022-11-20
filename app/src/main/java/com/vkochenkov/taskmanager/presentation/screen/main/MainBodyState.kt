package com.vkochenkov.taskmanager.presentation.screen.main

import com.vkochenkov.taskmanager.data.model.Task

sealed class MainBodyState {

    data class ShowContent(
        var tasksList: List<Task>?
    ) : MainBodyState()

    object ShowError: MainBodyState()
}
