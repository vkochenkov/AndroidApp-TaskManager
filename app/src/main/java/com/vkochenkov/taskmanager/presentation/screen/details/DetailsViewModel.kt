package com.vkochenkov.taskmanager.presentation.screen.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.base.BaseViewModel
import com.vkochenkov.taskmanager.presentation.utils.isNotNull
import kotlinx.coroutines.launch

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    val repository: TasksRepository
) : BaseViewModel() {

    private val taskIdFromNav: String? = savedStateHandle["id"]
    private var currentTask: Task? = null

    private var showDialogOnBack: Boolean = false
    private var showDialogOnDelete: Boolean = false

    private var _state: MutableState<DetailsBodyState> =
        mutableStateOf(DetailsBodyState.ShowEmpty)
    val state get() = _state

    val onAction = { action: DetailsActions ->
        when (action) {
            is DetailsActions.OnBackPressed -> onBackPressed(action.showDialog)
            is DetailsActions.OnTaskChanged -> onTaskChanged(action.task)
            is DetailsActions.SaveTask -> saveTaskAndLeaveScreen()
            is DetailsActions.CancelOnBackDialog -> cancelOnBackDialog()
            is DetailsActions.CancelOnDeleteDialog -> cancelOnDeleteDialog()
            is DetailsActions.DeleteTask -> deleteTask(action.showDialog)
        }
    }

    init {
        getTaskOrCreateNew()
    }

    private fun getTaskOrCreateNew() {
        if (taskIdFromNav.isNotNull()) {
            viewModelScope.launch {
                runCatching {
                    _state.value = DetailsBodyState.ShowLoading
                    repository.getTask(taskIdFromNav!!.toInt())
                }.onFailure {
                    _state.value = DetailsBodyState.ShowError
                }.onSuccess {
                    currentTask = it
                    _state.value = DetailsBodyState.ShowContent(it)
                }
            }
        } else {
            showDialogOnBack = true
            currentTask = repository.getNewTaskSample()
            currentTask?.let {
                _state.value = DetailsBodyState.ShowContent(
                    it
                )
            }
        }
    }

    private fun cancelOnBackDialog() {
        currentTask?.let { task ->
            _state.value = DetailsBodyState.ShowContent(
                task = task,
                showDialogOnBack = false
            )
        }
    }

    private fun cancelOnDeleteDialog() {
        currentTask?.let { task ->
            _state.value = DetailsBodyState.ShowContent(
                task = task,
                showDialogOnDelete= false
            )
        }
    }

    private fun saveTaskAndLeaveScreen() {
        currentTask?.let { task ->
            viewModelScope.launch {
                runCatching {
                    repository.saveTask(task)
                }.onFailure {
                    _state.value = DetailsBodyState.ShowError
                }.onSuccess {
                    _state.value = DetailsBodyState.ShowContent(task)
                    navController.popBackStack()
                }
            }
        }
    }

    private fun onBackPressed(showDialog: Boolean?) {
        showDialog?.let { showDialogOnBack = it }
        if (showDialogOnBack && currentTask != null) {
            _state.value = DetailsBodyState.ShowContent(
                task = currentTask!!,
                showDialogOnBack = true
            )
        } else {
            navController.popBackStack()
        }
    }

    private fun onTaskChanged(task: Task) {
        currentTask = task
        _state.value = DetailsBodyState.ShowContent(task)
        showDialogOnBack = true
    }

    private fun deleteTask(showDialog: Boolean?) {
        showDialog?.let { showDialogOnDelete = it }
        if (showDialogOnDelete && currentTask != null) {
            _state.value = DetailsBodyState.ShowContent(
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
                    _state.value = DetailsBodyState.ShowError
                }.onSuccess {
                    navController.popBackStack()
                }
            }
        }
    }
}