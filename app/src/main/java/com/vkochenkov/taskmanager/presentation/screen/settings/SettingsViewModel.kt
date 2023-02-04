package com.vkochenkov.taskmanager.presentation.screen.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vkochenkov.taskmanager.data.repos.StatusRepository
import com.vkochenkov.taskmanager.data.repos.TaskRepository
import com.vkochenkov.taskmanager.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class SettingsViewModel(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val statusRepository: StatusRepository
) : BaseViewModel() {

    var currentStatuses: List<String> = statusRepository.getStatuses()

    private var _state: MutableState<SettingsBodyState> =
        mutableStateOf(SettingsBodyState.Content(statuses = currentStatuses))
    val state: State<SettingsBodyState> get() = _state

    val onAction = { action: SettingsActions ->
        when (action) {
            is SettingsActions.BackPressed -> onBackPressed()
            is SettingsActions.AddNewStatus -> onAddNewStatus(action.status)
            is SettingsActions.DeleteStatus -> onDeleteStatus(action.index)
            is SettingsActions.ShowNewStatusDialog -> onShowNewStatusDialog()
            is SettingsActions.CanselNewStatusDialog -> onCancelNewStatusDialog()
            is SettingsActions.CanselCantDeleteStatusDialog -> onCancelCantDeleteStatusDialog()
        }
    }

    private fun onBackPressed() {
        navController.popBackStack()
    }

    private fun onShowNewStatusDialog() {
        _state.value = SettingsBodyState.Content(
            statuses = currentStatuses,
            showNewStatusDialog = true
        )
    }

    private fun onCancelNewStatusDialog() {
        _state.value = SettingsBodyState.Content(
            statuses = currentStatuses,
            showNewStatusDialog = false
        )
    }

    private fun onCancelCantDeleteStatusDialog() {
        _state.value = SettingsBodyState.Content(
            statuses = currentStatuses,
            showCantDeleteStatusDialog = null
        )
    }

    private fun onDeleteStatus(index: Int) {
        if (currentStatuses.size > 1) {
            viewModelScope.launch {
                _state.value = SettingsBodyState.Content(
                    statuses = currentStatuses,
                    loadingStatusIndex = index
                )
                val statusForDelete = currentStatuses.get(index)
                var isDelete = true
                taskRepository.getAllTasks().forEach {
                    if (it.status == statusForDelete) {
                        isDelete = false
                    }
                }
                if (isDelete) {
                    val modifiedStatuses = currentStatuses.toMutableList()
                    modifiedStatuses.removeAt(index)
                    statusRepository.rewriteStatuses(modifiedStatuses)
                    currentStatuses = modifiedStatuses
                    _state.value = SettingsBodyState.Content(
                        statuses = currentStatuses,
                    )
                } else {
                    _state.value = SettingsBodyState.Content(
                        statuses = currentStatuses,
                        showCantDeleteStatusDialog = SettingsBodyState.Content.ReasonCantDeleteStatus.SAME
                    )
                }
            }
        } else {
            _state.value = SettingsBodyState.Content(
                statuses = currentStatuses,
                showCantDeleteStatusDialog = SettingsBodyState.Content.ReasonCantDeleteStatus.LAST
            )
        }
    }

    private fun onAddNewStatus(status: String) {
        val modifiedStatuses = currentStatuses.toMutableList()
        modifiedStatuses.add(status)
        statusRepository.rewriteStatuses(modifiedStatuses)
        currentStatuses = modifiedStatuses
        _state.value = SettingsBodyState.Content(
            statuses = currentStatuses
        )
    }
}