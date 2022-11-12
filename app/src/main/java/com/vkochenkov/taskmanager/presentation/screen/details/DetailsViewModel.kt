package com.vkochenkov.taskmanager.presentation.screen.details

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

class DetailsViewModel(
    navHostController: NavHostController,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private var _state: MutableState<DetailsBodyState> = mutableStateOf(DetailsBodyState.ShowData("test"))
    val state get() = _state

    val onAction = { action: DetailsActions ->
        Log.d("vladd", "onAction invoke")

        when(action) {
            is DetailsActions.OnClick -> _state.value = DetailsBodyState.ShowData("new title")
        }
    }
}