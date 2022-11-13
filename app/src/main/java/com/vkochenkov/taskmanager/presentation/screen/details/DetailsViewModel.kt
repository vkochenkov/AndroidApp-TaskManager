package com.vkochenkov.taskmanager.presentation.screen.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

class DetailsViewModel(
    navHostController: NavHostController,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val taskId: String = checkNotNull(savedStateHandle["id"])

    private var _state: MutableState<DetailsBodyState> = mutableStateOf(DetailsBodyState.ShowData(taskId))
    val state get() = _state

    val onAction = { action: DetailsActions ->
        when(action) {
            is DetailsActions.OnClick -> _state.value = DetailsBodyState.ShowData("new title")
        }
    }
}