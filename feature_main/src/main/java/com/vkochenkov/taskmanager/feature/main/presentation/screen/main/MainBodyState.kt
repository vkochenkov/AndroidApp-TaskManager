package com.vkochenkov.taskmanager.feature.main.presentation.screen.main

import com.vkochenkov.taskmanager.base.data.model.Task

data class MainBodyState(
    val tasksList: List<Task> = listOf(),
    val statusesList: List<String> = listOf(),
    val isLoadingPage: Boolean = false,
    val isErrorPage: Boolean = false
)

