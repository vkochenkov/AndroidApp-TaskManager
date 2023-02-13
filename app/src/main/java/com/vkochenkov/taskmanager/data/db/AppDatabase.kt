package com.vkochenkov.taskmanager.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vkochenkov.taskmanager.data.db.converters.ListStringsConverter
import com.vkochenkov.taskmanager.data.db.converters.PriorityConverter
import com.vkochenkov.taskmanager.data.model.Task

@Database(entities = [Task::class], version = 5)
@TypeConverters(PriorityConverter::class, ListStringsConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}