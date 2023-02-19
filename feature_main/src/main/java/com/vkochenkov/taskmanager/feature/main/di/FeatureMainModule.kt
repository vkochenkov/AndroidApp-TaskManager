package com.vkochenkov.taskmanager.feature.main.di

import com.vkochenkov.taskmanager.feature.main.presentation.screen.details.DetailsViewModel
import com.vkochenkov.taskmanager.feature.main.presentation.screen.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureMainModule = module {

    // MainViewModel
    viewModel {
        MainViewModel(
            savedStateHandle = get(),
            taskDataService = get(),
            statusPreferences = get()
        )
    }

    // DetailsViewModel
    viewModel {
        DetailsViewModel(
            savedStateHandle = get(),
            taskDataService = get(),
            statusPreferences = get(),
            applicationContext = androidContext()
        )
    }
}