package com.vkochenkov.taskmanager.di

import android.content.Context
import androidx.room.Room
import com.vkochenkov.taskmanager.data.db.AppDatabase
import com.vkochenkov.taskmanager.data.repos.StatusRepository
import com.vkochenkov.taskmanager.data.repos.StatusRepository.Companion.STATUSES_PREFS_KEY
import com.vkochenkov.taskmanager.data.repos.TaskRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

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
    single { TaskRepository(get()) }

    // Status prefs
    single {
        androidContext().getSharedPreferences(STATUSES_PREFS_KEY, Context.MODE_PRIVATE)
    }

    // StatusRepository
    single {
        StatusRepository(get())
    }
}