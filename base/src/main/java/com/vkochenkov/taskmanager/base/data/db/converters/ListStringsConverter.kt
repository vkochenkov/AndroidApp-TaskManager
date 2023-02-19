package com.vkochenkov.taskmanager.base.data.db.converters

import androidx.room.TypeConverter
import com.vkochenkov.taskmanager.base.data.DefaultData.COMMON_DELIMITER
import com.vkochenkov.taskmanager.base.data.utils.asString

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