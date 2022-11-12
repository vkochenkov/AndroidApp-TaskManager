package com.vkochenkov.taskmanager.presentation.screen.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

class MainViewModel(
    navHostController: NavHostController,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private var _state: MutableState<MainBodyState> = mutableStateOf(MainBodyState.ShowData("str"))
    val state: State<MainBodyState> get() = _state

    val onAction = { action: MainActions ->
        Log.d("vladd", "onAction invoke")

        when(action) {
            is MainActions.OpenDetails -> {
                savedStateHandle.keys()
                navHostController.navigate("details")
            }
        }
    }
}