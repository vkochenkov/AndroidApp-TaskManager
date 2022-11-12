package com.vkochenkov.taskmanager.presentation.screen.main

sealed class MainBodyState {

    data class ShowData(
        var title: String
    ) : MainBodyState()
}
