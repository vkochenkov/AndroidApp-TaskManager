package com.vkochenkov.taskmanager.base.presentation

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<State, Action> : ViewModel() {

    /**
     * Should initialise in screen, after creating view model
     */
    lateinit var navController: NavHostController

    abstract val state: StateFlow<State>

//   ideas for improving. See https://github.com/wellingtoncabral/android-compose-mvi-navigation

//    private fun setState(function: State.() -> State) {
//        _state.value = _state.value.function()
//    }

//    private val _effects: Channel<Effect> = Channel()
//    val effects = _effects.receiveAsFlow()

//    private fun sendEffect(effect: Effect) {
//        viewModelScope.launch {
//            delay(100)
//            _effects.send(effect)
//        }
//    }

    abstract val onAction: (Action) -> Unit
}