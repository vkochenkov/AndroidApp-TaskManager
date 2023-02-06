package com.vkochenkov.taskmanager.data.repos

import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.data.db.TaskDao

class TaskRepository(
    private val dao: TaskDao
) {
    suspend fun getAllTasks(): List<Task> {
        return dao.getAll()
    }

    suspend fun getTasksByStatus(status: String): List<Task> {
        return dao.getByStatus(status)
    }

    suspend fun getTask(id: Int): Task? {
        return dao.get(id)
    }

    suspend fun saveTask(task: Task) {
        val updated = task.copy(updateDate = System.currentTimeMillis().toString())
        dao.insert(updated)
    }

    suspend fun saveTasks(tasks: List<Task>) {
        val updated: MutableList<Task> = ArrayList()
        tasks.forEach {
            updated.add(
                it.copy(updateDate = System.currentTimeMillis().toString())
            )
        }
        dao.insertAll(updated)
    }

    suspend fun deleteTask(task: Task) {
        dao.delete(task)
    }
}