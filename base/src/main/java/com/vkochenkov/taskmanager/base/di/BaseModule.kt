package com.vkochenkov.taskmanager.base.di

import android.content.Context
import androidx.room.Room
import com.vkochenkov.taskmanager.base.data.StatusPreferences
import com.vkochenkov.taskmanager.base.data.StatusPreferences.Companion.STATUSES_PREFS_KEY
import com.vkochenkov.taskmanager.base.data.TaskDataService
import com.vkochenkov.taskmanager.base.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val baseModule = module {

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

    // TaskRepository
    single { TaskDataService(get()) }

    // Status prefs
    single {
        androidContext().getSharedPreferences(STATUSES_PREFS_KEY, Context.MODE_PRIVATE)
    }

    // StatusRepository
    single {
        StatusPreferences(get())
    }
}