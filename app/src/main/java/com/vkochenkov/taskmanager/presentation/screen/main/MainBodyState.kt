package com.vkochenkov.taskmanager.presentation.screen.main

import com.vkochenkov.taskmanager.data.model.Task

sealed class MainBodyState {

    data class Content(
        val tasksList: List<Task>,
        val statusesList: List<String>
    ) : MainBodyState()

    object Empty: MainBodyState()

    object Error: MainBodyState()

    object Loading: MainBodyState()
}
