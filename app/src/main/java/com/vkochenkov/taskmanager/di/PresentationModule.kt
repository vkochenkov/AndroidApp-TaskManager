package com.vkochenkov.taskmanager.di

import com.vkochenkov.taskmanager.presentation.screen.details.DetailsViewModel
import com.vkochenkov.taskmanager.presentation.screen.main.MainViewModel
import com.vkochenkov.taskmanager.presentation.screen.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    // MainViewModel
    viewModel {
        MainViewModel(
            savedStateHandle = get(),
            taskRepository = get(),
            statusRepository = get()
        )
    }

    // DetailsViewModel
    viewModel {
        DetailsViewModel(
            savedStateHandle = get(),
            taskRepository = get(),
            statusRepository = get(),
            applicationContext = androidContext()
        )
    }

    // SettingsViewModel
    viewModel {
        SettingsViewModel(
            savedStateHandle = get(),
            statusRepository = get()
        )
    }
}