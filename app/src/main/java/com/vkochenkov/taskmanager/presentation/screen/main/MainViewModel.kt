package com.vkochenkov.taskmanager.presentation.screen.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.presentation.base.BaseViewModel
import com.vkochenkov.taskmanager.presentation.navigation.Destination
import kotlinx.coroutines.launch

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    val repository: TasksRepository
) : BaseViewModel() {

    private var _state: MutableState<MainBodyState> =
        mutableStateOf(MainBodyState.Loading)
    val state: State<MainBodyState> get() = _state

    val onAction = { action: MainActions ->
        when (action) {
            is MainActions.OpenDetails -> onOpenDetails(action.id)
            is MainActions.AddNewTask -> onAddNewTask()
            is MainActions.UpdateData -> onGetActiveTasks()
        }
    }

    private fun onGetActiveTasks() {
        viewModelScope.launch {
            runCatching {
                _state.value = MainBodyState.Loading
                repository.getAllTasks()
            }.onFailure {
                _state.value = MainBodyState.Error
            }.onSuccess {
                if (it.isNotEmpty()) {
                    _state.value = MainBodyState.Content(it)
                } else {
                    _state.value = MainBodyState.Empty
                }
            }
        }
    }

    private fun onAddNewTask() {
        navController.navigate(Destination.Details.passArguments(null))
    }

    private fun onOpenDetails(id: Int) {
        navController.navigate(Destination.Details.passArguments(id.toString()))
    }
}