package com.vkochenkov.taskmanager.presentation.screen.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.presentation.base.BaseViewModel
import com.vkochenkov.taskmanager.presentation.navigation.Destination

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    val repository: TasksRepository
) : BaseViewModel() {

    private var _state: MutableState<MainBodyState> =
        mutableStateOf(MainBodyState.ShowContent(null))
    val state: State<MainBodyState> get() = _state

    val onAction = { action: MainActions ->
        when (action) {
            is MainActions.OpenDetails -> {
                openDetails(action.id)
            }
            is MainActions.AddNewTask -> {
                addNewTask()
            }
        }
    }

    private fun addNewTask() {
        val d = null
        navController.navigate("${Destination.DETAILS}?id=null")
    }

    private fun openDetails(id: String) {
        navController.navigate("${Destination.DETAILS}?id=${id}")
    }

    init {
        _state.value = MainBodyState.ShowContent(
            repository.getAllTasks()
        )
    }
}