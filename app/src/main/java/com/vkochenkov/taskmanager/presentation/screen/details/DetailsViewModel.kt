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
    private var shouldSave: Boolean = false

    private var _state: MutableState<DetailsBodyState> =
        mutableStateOf(DetailsBodyState.EmptyContent)
    val state get() = _state

    val onAction = { action: DetailsActions ->
        when (action) {
            is DetailsActions.OnNavigateBack -> {
                onNavigateBack()
            }
            is DetailsActions.OnTaskChanged -> {
                shouldSave = true
            }
            else -> {}
        }
    }

    private fun onNavigateBack() {
        if (shouldSave && currentTask != null) {
            _state.value = DetailsBodyState.ShowContent(
                task = currentTask!!,
                isShowSaveDialog = true
            )
            shouldSave = false
        } else {
            navController.popBackStack()
        }
    }

    init {
        if (taskIdFromNav != null && taskIdFromNav != "null") {

            currentTask = repository.getTask(taskIdFromNav)
            currentTask?.let {
                _state.value = DetailsBodyState.ShowContent(
                    it
                )
            }

        } else {
            shouldSave = true

            currentTask = Task(
                (Math.random() * 1000).toInt().toString(),
                "w",
                "w",
                Task.Priority.LOW,
                Task.Status.TO_DO
            )
            currentTask?.let { task ->
                repository.saveTask(task)
                _state.value = DetailsBodyState.ShowContent(
                    task
                )
            }
        }
    }

    private fun onTaskChanged(task: Task) {
        currentTask = task
        repository.saveTask(task)
        _state.value = DetailsBodyState.ShowContent(
            task
        )
    }
}