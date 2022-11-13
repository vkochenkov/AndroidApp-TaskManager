package com.vkochenkov.taskmanager.data.model

data class Task(
    val id: String,
    val title: String,
    val description: String?,
    val priority: Priority,
    val status: Status
) {

    enum class Priority {
        LOW, NORMAL, HIGH
    }

    enum class Status {
        TO_DO, IN_PROGRESS, DONE
    }
}
