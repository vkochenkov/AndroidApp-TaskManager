package com.vkochenkov.taskmanager.presentation.screen.main

import com.vkochenkov.taskmanager.data.model.Task

sealed class MainBodyState {

    data class Content(
        var tasksList: List<Task>
    ) : MainBodyState()

    object Empty: MainBodyState()

    object Error: MainBodyState()

    object Loading: MainBodyState()
}
