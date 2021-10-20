package com.buildappswithvenkat.mytodoapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.buildappswithvenkat.mytodoapp.data.models.TodoData

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllData() : LiveData<List<TodoData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE )
    suspend fun insertData(todoData: TodoData)

    @Update
    suspend fun updateData(todoData: TodoData)

    @Delete
    suspend fun deleteItem(todoData: TodoData)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()
}