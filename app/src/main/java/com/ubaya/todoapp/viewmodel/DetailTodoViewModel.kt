package com.ubaya.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ubaya.todoapp.model.Todo
import com.ubaya.todoapp.model.TodoDatabase
import com.ubaya.todoapp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailTodoViewModel(application: Application): AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    val todoLD = MutableLiveData<Todo>()

    fun addTodoPakAndre(todo: Todo) { //punya bapaknya
        launch {
            val db = buildDb(getApplication())

            db.todoDao().insertAll(todo)
        }
    }

    fun addTodo(list: List<Todo>) { //dari ppt
        launch {
            val db = buildDb(getApplication())

            db.todoDao().insertAll(*list.toTypedArray())
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun fetch(uuid: Int) {
        launch {
            val db = buildDb(getApplication())
            todoLD.postValue(db.todoDao().selecTodo(uuid))
        }
    }

//    cara bapaknya
//    fun update(todo: Todo) {
//        launch {
//            buildDb(getApplication()).todoDao().updateTodo(todo)
//        }
//    }

    fun update(title:String, notes:String, priority:Int, id:Int) {
        launch {
            val db = buildDb(getApplication())
            db.todoDao().update(title, notes, priority, id)
        }
    }
}