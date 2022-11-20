package com.vkochenkov.taskmanager.presentation.screen.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.presentation.base.BaseViewModel
import com.vkochenkov.taskmanager.presentation.navigation.Destination
import kotlinx.coroutines.launch

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    val repository: TasksRepository
) : BaseViewModel() {

    private var _state: MutableState<MainBodyState> =
        mutableStateOf(MainBodyState.ShowContent(null))
    val state: State<MainBodyState> get() = _state

    val onAction = { action: MainActions ->
        when (action) {
            is MainActions.OnOpenDetails -> onOpenDetails(action.id)
            is MainActions.OnAddNewTask -> onAddNewTask()
        }
    }

    init {
        getAllTasks()
    }

    private fun getAllTasks() {
        viewModelScope.launch {
            _state.value = MainBodyState.ShowContent(
                repository.getAllTasks()
            )
        }
    }

    private fun onAddNewTask() {
        navController.navigate("${Destination.DETAILS}?id=null")
    }

    private fun onOpenDetails(id: Int) {
        Log.d("vladd", "nav to = ${Destination.DETAILS}?id=${id}")
        navController.navigate("${Destination.DETAILS}?id=${id}")
    }
}