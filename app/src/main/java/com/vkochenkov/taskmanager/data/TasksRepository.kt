package com.vkochenkov.taskmanager.data

import com.vkochenkov.taskmanager.data.model.Task

class TasksRepository(
    val dao: TaskDao
) {

    suspend fun getAllTasks(): List<Task> {
        return dao.getAll()
    }

    suspend fun getTask(id: Int): Task {
        return dao.get(id)
    }

    suspend fun saveTask(task: Task) {
        dao.insert(task)
    }

    suspend fun deleteTask(task: Task) {
        dao.delete(task)
    }

    fun getNewTaskSample(): Task {
        return Task(
            id = 0,
            title = "",
            description = "",
            priority = Task.Priority.LOW,
            status = Task.Status.TO_DO
        )
    }
}