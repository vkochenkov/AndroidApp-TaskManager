package com.vkochenkov.taskmanager.di

import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.presentation.screen.details.DetailsViewModel
import com.vkochenkov.taskmanager.presentation.screen.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { TasksRepository() }

    viewModel { params ->
        MainViewModel(
            navHostController = params.get(),
            savedStateHandle = get(),
            repository = get()
        )
    }
    viewModel { params ->
        DetailsViewModel(
            navHostController = params.get(),
            savedStateHandle = get(),
            repository = get()
        )
    }

}