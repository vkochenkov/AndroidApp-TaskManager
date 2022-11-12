package com.vkochenkov.taskmanager.presentation

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

open class BaseViewModel: ViewModel() {

    lateinit var navHostController: NavHostController

    fun prepareViewModel(navController: NavHostController) {
        navHostController = navController
    }
}