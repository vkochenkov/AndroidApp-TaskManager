package com.vkochenkov.taskmanager.presentation.screen.details

sealed class DetailsBodyState {

    data class ShowData(
        var title: String
    ): DetailsBodyState()
}
