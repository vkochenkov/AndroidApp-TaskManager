package com.vkochenkov.taskmanager.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vkochenkov.taskmanager.data.model.Task

@Database(entities = [Task::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}