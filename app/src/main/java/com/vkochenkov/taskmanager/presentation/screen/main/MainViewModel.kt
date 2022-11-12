package com.vkochenkov.taskmanager.presentation.screen.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.vkochenkov.taskmanager.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle
): BaseViewModel() {

    private var _state: MutableState<MainBodyState> = mutableStateOf(MainBodyState.ShowData("str"))
    val state: State<MainBodyState> get() = _state

    val onAction = { action: MainActions ->
        Log.d("vladd", "onAction invoke")

        when(action) {
            is MainActions.OpenDetails -> {
                navHostController.navigate("details")
            }
        }
    }
}