package com.vkochenkov.taskmanager.presentation.screen.details

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.base.BaseViewModel

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    val repository: TasksRepository
) : BaseViewModel() {

    private val taskIdFromNav: String? = savedStateHandle["id"]
    private var currentTask: Task? = null
    private var showDialogOnBack: Boolean = false

    private var _state: MutableState<DetailsBodyState> =
        mutableStateOf(DetailsBodyState.EmptyContent)
    val state get() = _state

    val onAction = { action: DetailsActions ->
        when (action) {
            is DetailsActions.OnBackPressed -> onBackPressed(action.showDialog)
            is DetailsActions.OnTaskChanged -> onTaskChanged(action.task)
            is DetailsActions.OnSaveTask -> onSaveAndLeave()
            is DetailsActions.OnCancelDialog -> onCancelDialog()
        }
    }

    init {
        getTaskOrCreateNew()
    }

    private fun getTaskOrCreateNew() {
        if (taskIdFromNav != null && taskIdFromNav != "null") {
            currentTask = repository.getTask(taskIdFromNav)
        } else {
            showDialogOnBack = true
            currentTask = Task(
                (Math.random() * 1000).toInt().toString(),
                "new",
                "new",
                Task.Priority.LOW,
                Task.Status.TO_DO
            )
        }
        currentTask?.let {
            _state.value = DetailsBodyState.ShowContent(
                it
            )
        }
    }

    private fun onCancelDialog() {
        Log.d("vladd", "showDialogOnBack = $showDialogOnBack")
        currentTask?.let { task ->
            _state.value = DetailsBodyState.ShowContent(
                task = task,
                showDialogOnBack = false
            )
        }
    }

    private fun onSaveAndLeave() {
        currentTask?.let { task ->
            repository.saveTask(task)
            _state.value = DetailsBodyState.ShowContent(
                task
            )
        }
        navController.popBackStack()
    }

    private fun onBackPressed(showDialog: Boolean?) {
        Log.d("vladd", "showDialogOnBack = $showDialogOnBack")
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
        _state.value = DetailsBodyState.ShowContent(
            task
        )
        showDialogOnBack = true
    }
}