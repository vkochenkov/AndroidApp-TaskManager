package com.vkochenkov.taskmanager.base.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.vkochenkov.taskmanager.base.data.model.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM task ORDER BY priority DESC, updateDate DESC")
    suspend fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE status like :status")
    suspend fun getByStatus(status: String): List<Task>

    @Insert(onConflict = REPLACE)
    suspend fun insert(task: Task)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(tasks: List<Task>)

    @Query("SELECT * FROM task WHERE id=:id")
    suspend fun get(id: Int): Task?

    @Delete
    suspend fun delete(task: Task)
}