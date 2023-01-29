package com.vkochenkov.taskmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val creationDate: String,
    val updateDate: String,
    val title: String,
    val description: String?,
    val priority: Priority,
    val status: Status,
    val notificationTime: Long?
) {

    enum class Priority {
        LOW,
        NORMAL,
        HIGH
    }

    enum class Status {
        TO_DO,
        IN_PROGRESS,
        REVIEW,
        DONE
    }
}
