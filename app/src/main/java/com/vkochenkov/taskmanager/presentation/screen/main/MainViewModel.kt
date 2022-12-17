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
        mutableStateOf(MainBodyState.ShowLoading)
    val state: State<MainBodyState> get() = _state

    val onAction = { action: MainActions ->
        when (action) {
            is MainActions.OpenDetails -> openDetails(action.id)
            is MainActions.AddNewTask -> addNewTask()
            is MainActions.UpdateData -> getActiveTasks()
        }
    }

    private fun getActiveTasks() {
        viewModelScope.launch {
            runCatching {
                _state.value = MainBodyState.ShowLoading
                repository.getAllTasks()
            }.onFailure {
                _state.value = MainBodyState.ShowError
            }.onSuccess {
                if (it.isNotEmpty()) {
                    _state.value = MainBodyState.ShowContent(it)
                } else {
                    _state.value = MainBodyState.ShowEmpty
                }
            }
        }
    }

    private fun addNewTask() {
        navController.navigate(Destination.Details.passArguments(null))
    }

    private fun openDetails(id: Int) {
        navController.navigate(Destination.Details.passArguments(id.toString()))
    }
}