package com.vkochenkov.taskmanager.di

import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.presentation.screen.details.DetailsViewModel
import com.vkochenkov.taskmanager.presentation.screen.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { TasksRepository() }

    viewModel {
        MainViewModel(
            savedStateHandle = get(),
            repository = get()
        )
    }
    viewModel {
        DetailsViewModel(
            savedStateHandle = get(),
            repository = get()
        )
    }

}