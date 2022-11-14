package com.vkochenkov.taskmanager.presentation.screen.details

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.data.model.Task

class DetailsViewModel(
    val navController: NavHostController,
    savedStateHandle: SavedStateHandle,
    val repository: TasksRepository
) : ViewModel() {

    private val taskId: String = checkNotNull(savedStateHandle["id"])
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
        Log.d("vladd", "id = $taskId")
        currentTask = repository.getTask(taskId)
        _state.value = DetailsBodyState.ShowContent(
            currentTask
        )
    }

    private fun onTaskChanged(task: Task) {
        currentTask = task
        repository.saveTask(task)
        _state.value = DetailsBodyState.ShowContent(
            currentTask
        )
    }
}