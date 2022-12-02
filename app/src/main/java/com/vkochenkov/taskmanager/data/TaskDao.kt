package com.vkochenkov.taskmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.vkochenkov.taskmanager.data.model.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM task ORDER BY updateDate DESC")
    suspend fun getAll(): List<Task>

    @Insert(onConflict = REPLACE)
    suspend fun insert(task: Task)

    @Query("SELECT * FROM task WHERE id=:id")
    suspend fun get(id: Int): Task

    @Delete
    suspend fun delete(task: Task)
}