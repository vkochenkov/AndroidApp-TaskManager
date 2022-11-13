package com.vkochenkov.taskmanager.data

import com.vkochenkov.taskmanager.data.model.Task

class TasksRepository {

    fun getAllTasks(): List<Task> {
        return listOf(
            Task(
                "1",
                "погулять с собакой",
                "описание описание описание описание",
                Task.Priority.LOW,
                Task.Status.IN_PROGRESS
            ),
            Task(
                "2",
                "поесть",
                "описание описание описание описание",
                Task.Priority.HIGH,
                Task.Status.IN_PROGRESS
            ),
            Task(
                "3",
                "поспать",
                "описание описание м описание",
                Task.Priority.NORMAL,
                Task.Status.IN_PROGRESS
            ),
        )
    }
}