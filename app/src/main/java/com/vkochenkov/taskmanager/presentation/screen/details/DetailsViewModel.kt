package com.vkochenkov.taskmanager.presentation.screen.details

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.vkochenkov.taskmanager.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(): BaseViewModel() {

    private var _state: MutableState<DetailsBodyState> = mutableStateOf(DetailsBodyState.ShowData("test"))
    val state get() = _state

    val onAction = { action: DetailsActions ->
        Log.d("vladd", "onAction invoke")

        when(action) {
            is DetailsActions.OnClick -> _state.value = DetailsBodyState.ShowData("new title")
        }
    }
}