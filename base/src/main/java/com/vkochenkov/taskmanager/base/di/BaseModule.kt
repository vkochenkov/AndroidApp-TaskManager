package com.vkochenkov.taskmanager.base.di

import android.content.Context
import androidx.room.Room
import com.vkochenkov.taskmanager.base.data.StatusPreferences.Companion.STATUSES_PREFS_KEY
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val baseModule = module {

    // AppDatabase
    single {
        Room.databaseBuilder(
            androidContext(),
            com.vkochenkov.taskmanager.base.data.db.AppDatabase::class.java, "task_manager_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    // TaskDao
    single {
        val database = get<com.vkochenkov.taskmanager.base.data.db.AppDatabase>()
        database.taskDao()
    }

    // TaskRepository
    single { com.vkochenkov.taskmanager.base.data.TaskDataService(get()) }

    // Status prefs
    single {
        androidContext().getSharedPreferences(STATUSES_PREFS_KEY, Context.MODE_PRIVATE)
    }

    // StatusRepository
    single {
        com.vkochenkov.taskmanager.base.data.StatusPreferences(get())
    }
}