package com.vkochenkov.taskmanager.presentation.screen.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.presentation.navigation.Destination

class MainViewModel(
    val navController: NavHostController,
    savedStateHandle: SavedStateHandle,
    val repository: TasksRepository
) : ViewModel() {

    private var _state: MutableState<MainBodyState> = mutableStateOf(MainBodyState.ShowContent(null))
    val state: State<MainBodyState> get() = _state

    val onAction = { action: MainActions ->
        when (action) {
            is MainActions.OpenDetails -> {
                navController.navigate("${Destination.DETAILS}?id=${action.id}")
            }
        }
    }

    init {
        _state.value = MainBodyState.ShowContent(
            repository.getAllTasks()
        )
    }
}