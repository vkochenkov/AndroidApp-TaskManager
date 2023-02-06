package com.vkochenkov.taskmanager.presentation.screen.main

import com.vkochenkov.taskmanager.data.model.Task

data class MainBodyState(
    val tasksList: List<Task> = listOf(),
    val statusesList: List<String> = listOf(),
    val isLoadingPage: Boolean = false,
    val isErrorPage: Boolean = false
)

