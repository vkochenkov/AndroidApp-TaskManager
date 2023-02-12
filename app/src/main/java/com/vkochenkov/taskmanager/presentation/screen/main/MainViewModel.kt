package com.vkochenkov.taskmanager.presentation.screen.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vkochenkov.taskmanager.data.repos.StatusRepository
import com.vkochenkov.taskmanager.data.repos.TaskRepository
import com.vkochenkov.taskmanager.presentation.base.BaseViewModel
import com.vkochenkov.taskmanager.presentation.navigation.Destination
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    val taskRepository: TaskRepository,
    val statusRepository: StatusRepository
) : BaseViewModel<MainBodyState, MainActions>() {

    private var _state: MutableState<MainBodyState> =
        mutableStateOf(MainBodyState(isLoadingPage = true))
    override val state: State<MainBodyState> get() = _state

    override val onAction = { action: MainActions ->
        when (action) {
            is MainActions.OpenDetails -> onOpenDetails(action.id)
            is MainActions.AddNewTask -> onAddNewTask()
            is MainActions.UpdateData -> onUpdateData()
            is MainActions.OpenSettings -> onOpenSettings()
            is MainActions.Exit -> exitProcess(-1)
        }
    }

    private fun onUpdateData() {
        val statuses = statusRepository.getStatuses()

        viewModelScope.launch {
            runCatching {
                _state.value = MainBodyState(isLoadingPage = true)
                taskRepository.getAllTasks()
            }.onFailure {
                _state.value = MainBodyState(isErrorPage = true)
            }.onSuccess {
                _state.value = MainBodyState(it, statuses)
            }
        }
    }

    private fun onAddNewTask() {
        navController.navigate(Destination.Details.passArguments(null))
    }

    private fun onOpenDetails(id: Int) {
        navController.navigate(Destination.Details.passArguments(id.toString()))
    }

    private fun onOpenSettings() {
        navController.navigate(Destination.Settings.route)
    }
}