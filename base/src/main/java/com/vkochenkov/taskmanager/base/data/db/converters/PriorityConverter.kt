package com.vkochenkov.taskmanager.base.data.db.converters

import androidx.room.TypeConverter
import com.vkochenkov.taskmanager.base.data.model.Task

class PriorityConverter {

    @TypeConverter
    fun toPriority(value: Int) = enumValues<Task.Priority>()[value]

    @TypeConverter
    fun fromPriority(value: Task.Priority) = value.ordinal
}