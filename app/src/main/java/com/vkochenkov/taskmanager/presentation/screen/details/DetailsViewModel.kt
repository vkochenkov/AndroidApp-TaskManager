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

    private var _state: MutableState<DetailsBodyState> =
        mutableStateOf(DetailsBodyState.ShowContent(null))
    val state get() = _state

    val onAction = { action: DetailsActions ->
        when (action) {
            is DetailsActions.OnNavigateBack -> {
                onNavigateBack()
            }
            is DetailsActions.OnTaskChanged -> {
                onTaskChanged(action.task)
            }
            else -> {}
        }
    }

    private fun onNavigateBack() {
        navController.popBackStack()
    }

    init {
        Log.d("vladd", "id = $taskIdFromNav")
        if (taskIdFromNav != null && taskIdFromNav != "null") {
            Log.d("vladd", "if")

            currentTask = repository.getTask(taskIdFromNav)
            _state.value = DetailsBodyState.ShowContent(
                currentTask
            )
        } else {
            Log.d("vladd", "else")

            currentTask = Task(
                (Math.random()*1000).toInt().toString(),
                "w",
                "w",
                Task.Priority.LOW,
                Task.Status.TO_DO
            )
            Log.d("vladd", "currentTask = $currentTask")
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
            currentTask
        )
    }
}