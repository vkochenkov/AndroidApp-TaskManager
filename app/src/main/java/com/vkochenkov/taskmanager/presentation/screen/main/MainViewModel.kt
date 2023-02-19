package com.vkochenkov.taskmanager.presentation.screen.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vkochenkov.taskmanager.data.StatusPreferences
import com.vkochenkov.taskmanager.data.TaskDataService
import com.vkochenkov.taskmanager.presentation.base.BaseViewModel
import com.vkochenkov.taskmanager.presentation.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    val taskDataService: TaskDataService,
    val statusPreferences: StatusPreferences
) : BaseViewModel<MainBodyState, MainActions>() {

    private var _state: MutableStateFlow<MainBodyState> =
        MutableStateFlow(MainBodyState(isLoadingPage = true))
    override val state: StateFlow<MainBodyState> get() = _state.asStateFlow()

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
        val statuses = statusPreferences.getStatuses()

        viewModelScope.launch {
            runCatching {
                _state.value = MainBodyState(isLoadingPage = true)
                taskDataService.getAllTasks()
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