package com.vkochenkov.taskmanager.data

import com.vkochenkov.taskmanager.data.model.Task

class TasksRepository {

    fun getAllTasks(): List<Task> {
        return listOf(
            Task("1","number 1", "ddd dd", Task.Priority.NORMAL, Task.Status.IN_PROGRESS),
            Task("2","number 2", "dddde rere dd", Task.Priority.NORMAL, Task.Status.IN_PROGRESS)
        )
    }
}