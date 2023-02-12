package com.vkochenkov.taskmanager.data.db

import androidx.room.TypeConverter
import com.vkochenkov.taskmanager.data.DefaultData.COMMON_DELIMITER
import com.vkochenkov.taskmanager.data.utils.asString

class ListStringsConverter {

    @TypeConverter
    fun toListStrings(value: String): List<String> {
        return if (value.isNotEmpty()) value.split(COMMON_DELIMITER).toList()
        else listOf()
    }

    @TypeConverter
    fun fromListStrings(value: List<String>): String {
        return if (value.isNotEmpty()) value.asString(COMMON_DELIMITER)
        else ""
    }
}