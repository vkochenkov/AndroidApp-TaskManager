package com.vkochenkov.taskmanager.presentation.screen.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.base.BaseViewModel
import com.vkochenkov.taskmanager.presentation.navigation.Destination
import com.vkochenkov.taskmanager.presentation.utils.isNotNull
import kotlinx.coroutines.launch

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    val repository: TasksRepository
) : BaseViewModel() {

    private val taskIdFromNav: String? = savedStateHandle[Destination.Details.argument1]
    private var currentTask: Task? = null

    private var showDialogOnBack: Boolean = false
    private var showDialogOnDelete: Boolean = false

    private var _state: MutableState<DetailsBodyState> =
        mutableStateOf(DetailsBodyState.Loading)
    val state get() = _state

    val onAction = { action: DetailsActions ->
        when (action) {
            is DetailsActions.BackPressed -> onBackPressed(action.showDialog)
            is DetailsActions.TaskChanged -> onTaskChanged(action.task)
            is DetailsActions.SaveTask -> onSaveTask()
            is DetailsActions.CancelBackDialog -> onCancelBackDialog()
            is DetailsActions.CancelDeleteDialog -> onCancelDeleteDialog()
            is DetailsActions.DeleteTask -> onDeleteTask(action.showDialog)
        }
    }

    init {
        getTaskOrCreateNew()
    }

    private fun getTaskOrCreateNew() {
        if (taskIdFromNav.isNotNull()) {
            viewModelScope.launch {
                runCatching {
                    _state.value = DetailsBodyState.Loading
                    repository.getTask(taskIdFromNav!!.toInt())
                }.onFailure {
                    _state.value = DetailsBodyState.Error
                }.onSuccess {
                    currentTask = it
                    _state.value = DetailsBodyState.Content(it)
                }
            }
        } else {
            showDialogOnBack = true
            currentTask = repository.getNewTaskSample()
            currentTask?.let {
                _state.value = DetailsBodyState.Content(
                    it
                )
            }
        }
    }

    private fun onCancelBackDialog() {
        currentTask?.let { task ->
            _state.value = DetailsBodyState.Content(
                task = task,
                showDialogOnBack = false
            )
        }
    }

    private fun onCancelDeleteDialog() {
        currentTask?.let { task ->
            _state.value = DetailsBodyState.Content(
                task = task,
                showDialogOnDelete = false
            )
        }
    }

    private fun onSaveTask() {
        currentTask?.let { task ->
            if (task.title.isEmpty() || task.title.length > TITLE_MAX_LENGTH) {
                _state.value = DetailsBodyState.Content(task, showTitleValidation = true)
            } else if (task.description != null && task.description.length > DESCRIPTION_MAX_LENGTH) {
                _state.value = DetailsBodyState.Content(task, showDescriptionValidation = true)
            } else {
                viewModelScope.launch {
                    runCatching {
                        repository.saveTask(task)
                    }.onFailure {
                        _state.value = DetailsBodyState.Error
                    }.onSuccess {
                        _state.value = DetailsBodyState.Content(task)
                        navController.popBackStack()
                    }
                }
            }
        }
    }

    private fun onBackPressed(showDialog: Boolean?) {
        showDialog?.let { showDialogOnBack = it }
        if (showDialogOnBack && currentTask != null) {
            _state.value = DetailsBodyState.Content(
                task = currentTask!!,
                showDialogOnBack = true
            )
        } else {
            navController.popBackStack()
        }
    }

    private fun onTaskChanged(task: Task) {
        currentTask = task
        _state.value = DetailsBodyState.Content(task)
        showDialogOnBack = true
    }

    private fun onDeleteTask(showDialog: Boolean?) {
        showDialog?.let { showDialogOnDelete = it }
        if (showDialogOnDelete && currentTask != null) {
            _state.value = DetailsBodyState.Content(
                task = currentTask!!,
                showDialogOnDelete = true
            )
        } else {
            viewModelScope.launch {
                runCatching {
                    currentTask?.let {
                        repository.deleteTask(it)
                    }
                }.onFailure {
                    _state.value = DetailsBodyState.Error
                }.onSuccess {
                    navController.popBackStack()
                }
            }
        }
    }

    companion object {
        const val TITLE_MAX_LENGTH = 50
        const val DESCRIPTION_MAX_LENGTH = 300
    }
}