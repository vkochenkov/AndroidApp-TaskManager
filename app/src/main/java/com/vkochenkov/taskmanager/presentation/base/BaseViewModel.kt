package com.vkochenkov.taskmanager.presentation.base

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

open class BaseViewModel : ViewModel() {

    internal lateinit var navController: NavHostController
}