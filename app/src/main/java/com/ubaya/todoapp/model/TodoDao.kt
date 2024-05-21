package com.ubaya.todoapp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg todo: Todo)

    @Query("SELECT * FROM todo ORDER BY priority DESC")
    fun selectAllTodo(): List<Todo>

    @Query("SELECT * FROM todo WHERE uuid = :id")
    fun selecTodo(id:Int): Todo

    @Query("UPDATE todo SET title = :title, notes = :notes, priority = :priority WHERE uuid = :id")
    fun update(title: String, notes: String, priority: Int, id: Int)

    @Delete
    fun deleteTodo(todo: Todo)
}