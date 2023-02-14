package com.vkochenkov.taskmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Db and domain model. I had not made special model for domain because this is a little project
@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = System.currentTimeMillis().toInt(),
    val creationDate: String,
    val updateDate: String,
    val title: String,
    val description: String? = null,
    val priority: Priority,
    val status: String,
    val notificationTime: Long? = null,
    val attachments: List<String> = listOf()
) {

    enum class Priority {
        LOW,
        NORMAL,
        HIGH
    }
}
