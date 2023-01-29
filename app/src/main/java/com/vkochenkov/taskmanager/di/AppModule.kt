package com.vkochenkov.taskmanager.di

import androidx.room.Room
import com.vkochenkov.taskmanager.data.AppDatabase
import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.presentation.screen.details.DetailsViewModel
import com.vkochenkov.taskmanager.presentation.screen.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // AppDatabase
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "task_manager_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    // TaskDao
    single {
        val database = get<AppDatabase>()
        database.taskDao()
    }

    // Repository
    single { TasksRepository(get()) }

    // MainViewModel
    viewModel {
        MainViewModel(
            savedStateHandle = get(),
            repository = get()
        )
    }

    // DetailsViewModel
    viewModel {
        DetailsViewModel(
            savedStateHandle = get(),
            repository = get(),
            applicationContext = androidContext()
        )
    }
}