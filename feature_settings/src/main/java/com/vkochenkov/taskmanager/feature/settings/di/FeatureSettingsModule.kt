package com.vkochenkov.taskmanager.feature.settings.di

import com.vkochenkov.taskmanager.feature.settings.presentation.screen.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureSettingsModule = module {

    // SettingsViewModel
    viewModel {
        SettingsViewModel(
            savedStateHandle = get(),
            taskDataService = get(),
            statusPreferences = get()
        )
    }
}