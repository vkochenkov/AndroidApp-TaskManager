package com.vkochenkov.taskmanager.presentation.screen.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.data.StatusPreferences
import com.vkochenkov.taskmanager.data.TaskDataService
import com.vkochenkov.taskmanager.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    savedStateHandle: SavedStateHandle,
    private val taskDataService: TaskDataService,
    private val statusPreferences: StatusPreferences
) : BaseViewModel<SettingsBodyState, SettingsActions>() {

    var currentStatuses: List<String> = statusPreferences.getStatuses()

    private var _state: MutableStateFlow<SettingsBodyState> =
        MutableStateFlow(SettingsBodyState(statuses = currentStatuses))
    override val state: StateFlow<SettingsBodyState> get() = _state.asStateFlow()

    override val onAction = { action: SettingsActions ->
        when (action) {
            is SettingsActions.BackPressed -> onBackPressed()
            is SettingsActions.AddNewStatus -> onAddNewStatus(action.status)
            is SettingsActions.DeleteStatus -> onDeleteStatus(action.index)
            is SettingsActions.ShowNewStatusDialog -> onShowNewStatusDialog()
            is SettingsActions.CanselNewStatusDialog -> onCancelNewStatusDialog()
            is SettingsActions.CanselCantDeleteStatusDialog -> onCancelCantDeleteStatusDialog()
            is SettingsActions.CanselRenameStatusDialog -> onCancelRenameStatusDialog()
            is SettingsActions.RenameStatus -> onRenameStatus(action.status, action.index)
            is SettingsActions.ShowRenameStatusDialog -> onShowRenameStatusDialog(action.index)
        }
    }

    private fun onBackPressed() {
        navController.popBackStack()
    }

    private fun onShowNewStatusDialog() {
        _state.value = SettingsBodyState(
            statuses = currentStatuses,
            showNewStatusDialog = true
        )
    }

    private fun onShowRenameStatusDialog(index: Int) {
        _state.value = SettingsBodyState(
            statuses = currentStatuses,
            renameStatusIndex = index
        )
    }

    private fun onCancelNewStatusDialog() {
        _state.value = SettingsBodyState(
            statuses = currentStatuses,
            showNewStatusDialog = false
        )
    }

    private fun onCancelRenameStatusDialog() {
        _state.value = SettingsBodyState(
            statuses = currentStatuses,
            renameStatusIndex = null
        )
    }

    private fun onCancelCantDeleteStatusDialog() {
        _state.value = SettingsBodyState(
            statuses = currentStatuses,
            showCantDeleteStatusDialog = null
        )
    }

    private fun onDeleteStatus(index: Int) {
        if (currentStatuses.size > 1) {
            viewModelScope.launch {
                _state.value = SettingsBodyState(
                    statuses = currentStatuses,
                    loadingStatusIndex = index
                )
                val statusForDelete = currentStatuses.get(index)
                var isDelete = true
                taskDataService.getAllTasks().forEach {
                    if (it.status == statusForDelete) {
                        isDelete = false
                    }
                }
                if (isDelete) {
                    val modifiedStatuses = currentStatuses.toMutableList()
                    modifiedStatuses.removeAt(index)
                    statusPreferences.rewriteStatuses(modifiedStatuses)
                    currentStatuses = modifiedStatuses
                    _state.value = SettingsBodyState(
                        statuses = currentStatuses,
                    )
                } else {
                    _state.value = SettingsBodyState(
                        statuses = currentStatuses,
                        showCantDeleteStatusDialog = SettingsBodyState.ReasonCantDeleteStatus.SAME
                    )
                }
            }
        } else {
            _state.value = SettingsBodyState(
                statuses = currentStatuses,
                showCantDeleteStatusDialog = SettingsBodyState.ReasonCantDeleteStatus.LAST
            )
        }
    }

    private fun onAddNewStatus(status: String) {
        val modifiedStatuses = currentStatuses.toMutableList()
        modifiedStatuses.add(status)
        statusPreferences.rewriteStatuses(modifiedStatuses)
        currentStatuses = modifiedStatuses
        _state.value = SettingsBodyState(
            statuses = currentStatuses
        )
    }

    private fun onRenameStatus(status: String, index: Int) {
        val oldStatusName = currentStatuses.get(index)

        viewModelScope.launch {
            _state.value = SettingsBodyState(
                statuses = currentStatuses,
                loadingStatusIndex = index
            )
            runCatching {
                taskDataService.getTasksByStatus(oldStatusName)
            }.onFailure {
                // ignore error
                _state.value = SettingsBodyState(
                    statuses = currentStatuses
                )
            }.onSuccess { tasks ->
                val updatedTasks = ArrayList<Task>()
                tasks.forEach {
                    updatedTasks.add(
                        it.copy(
                            status = status
                        )
                    )
                }
                runCatching {
                    taskDataService.saveTasks(updatedTasks)
                }.onFailure {
                    // ignore error
                    _state.value = SettingsBodyState(
                        statuses = currentStatuses
                    )
                }.onSuccess {
                    val modifiedStatuses = currentStatuses.toMutableList()
                    modifiedStatuses.removeAt(index)
                    modifiedStatuses.add(index, status)
                    currentStatuses = modifiedStatuses
                    statusPreferences.rewriteStatuses(modifiedStatuses)
                    _state.value = SettingsBodyState(
                        statuses = currentStatuses
                    )
                }
            }
        }
    }
}