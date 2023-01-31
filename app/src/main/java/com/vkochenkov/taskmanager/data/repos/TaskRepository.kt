package com.vkochenkov.taskmanager.data.repos

import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.data.DefaultData.defaultStatuses
import com.vkochenkov.taskmanager.data.db.TaskDao

class TaskRepository(
    val dao: TaskDao
) {
    suspend fun getAllTasks(): List<Task> {
        return dao.getAll()
    }

    suspend fun getTask(id: Int): Task? {
        return dao.get(id)
    }

    suspend fun saveTask(task: Task) {
        val updated = task.copy(updateDate = System.currentTimeMillis().toString())
        dao.insert(updated)
    }

    suspend fun deleteTask(task: Task) {
        dao.delete(task)
    }

    fun getNewTaskSample(): Task {
        val currentDate = System.currentTimeMillis().toString()
        return Task(
            id = 0,
            title = "New task",
            description = "",
            priority = Task.Priority.NORMAL,
            status = defaultStatuses.get(0),
            creationDate = currentDate,
            updateDate = currentDate,
            notificationTime = null
        )
    }
}