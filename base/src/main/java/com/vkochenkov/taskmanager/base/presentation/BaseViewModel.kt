package com.vkochenkov.taskmanager.base.presentation

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<St, Act> : ViewModel() {

    /**
     * Should initialise in screen, after creating view model
     */
    lateinit var navController: NavHostController

    abstract val state: StateFlow<St>

    abstract val onAction: (Act) -> Unit
}