package com.vkochenkov.taskmanager.presentation.screen.details

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.presentation.screen.main.MainBodyState

class DetailsViewModel(
    navHostController: NavHostController,
    savedStateHandle: SavedStateHandle,
    repository: TasksRepository
) : ViewModel() {

    private val taskId: String = checkNotNull(savedStateHandle["id"])

    private var _state: MutableState<DetailsBodyState> =
        mutableStateOf(DetailsBodyState.ShowContent(null))
    val state get() = _state

    val onAction = { action: DetailsActions ->
        when (action) {

        }
    }

    init {
        Log.d("vladd", "id = $taskId")
        _state.value = DetailsBodyState.ShowContent(
            repository.getTask(taskId)
        )
    }
}