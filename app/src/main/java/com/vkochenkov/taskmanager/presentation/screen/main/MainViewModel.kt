package com.vkochenkov.taskmanager.presentation.screen.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private var _state: MutableState<MainBodyState> = mutableStateOf(MainBodyState.ShowData("test"))
    val state get() = _state

    val onAction = { action: MainActions ->
        Log.d("vladd", "onAction invoke")

        when(action) {
            is MainActions.OnClick -> _state.value = MainBodyState.ShowData("new title")
        }
    }
}