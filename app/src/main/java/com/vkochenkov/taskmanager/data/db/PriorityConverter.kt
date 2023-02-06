package com.vkochenkov.taskmanager.data.db

import androidx.room.TypeConverter
import com.vkochenkov.taskmanager.data.model.Task

class PriorityConverter {

    @TypeConverter
    fun toPriority(value: Int) = enumValues<Task.Priority>()[value]

    @TypeConverter
    fun fromPriority(value: Task.Priority) = value.ordinal
}