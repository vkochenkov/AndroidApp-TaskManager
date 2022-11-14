package com.vkochenkov.taskmanager.data

import com.vkochenkov.taskmanager.data.model.Task

class TasksRepository {

    private var data = listOf(
        Task(
            "0",
            "погулять с собакой погулять с собакой",
            "описание описание описание описание",
            Task.Priority.LOW,
            Task.Status.IN_PROGRESS
        ),
        Task(
            "1",
            "поесть поесть поесть поесть поесть поесть",
            "описание описание описание описание",
            Task.Priority.HIGH,
            Task.Status.IN_PROGRESS
        ),
        Task(
            "2",
            "поспать поспать поспать поспать поспать",
            "описание описание м описание",
            Task.Priority.NORMAL,
            Task.Status.IN_PROGRESS
        ),
    )

    fun getAllTasks(): List<Task> {
        return data
    }

    fun getTask(id: String): Task? {
        return try {
            data[id.toInt()]
        } catch (ex: Exception) {
            null
        }
    }
}