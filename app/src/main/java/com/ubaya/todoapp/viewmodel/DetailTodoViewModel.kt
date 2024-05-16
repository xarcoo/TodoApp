package com.ubaya.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ubaya.todoapp.model.Todo
import com.ubaya.todoapp.model.TodoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailTodoViewModel(application: Application): AndroidViewModel(application), CoroutineScope {
    private val job = Job()

    fun addTodoPakAndre(todo: Todo) { //punya bapaknya
        launch {
            val db = TodoDatabase.buildDatabase(
                getApplication()
            )

            db.todoDao().insertAll(todo)
        }
    }

    fun addTodo(list: List<Todo>) { //dari ppt
        launch {
            val db = TodoDatabase.buildDatabase(
                getApplication()
            )

            db.todoDao().insertAll(*list.toTypedArray())
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}