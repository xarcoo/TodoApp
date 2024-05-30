package com.ubaya.todoapp.view

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.todoapp.databinding.TodoItemLayoutBinding
import com.ubaya.todoapp.model.Todo

class TodoListAdapter(val todoList:ArrayList<Todo>, val adapterOnClick: (Todo) -> Unit): RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {
    class TodoViewHolder(var binding: TodoItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        var binding = TodoItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TodoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.binding.checkTask.text = todoList[position].title

        // ini salah, harusnya kalo isDone == 1, ga muncul
        if (todoList[position].isDone.equals(1)) {
            holder.binding.checkTask.paintFlags = holder.binding.checkTask.paintFlags or STRIKE_THRU_TEXT_FLAG
            holder.binding.checkTask.isEnabled = false
            holder.binding.checkTask.isChecked = true
        } else {
            holder.binding.checkTask.isChecked = false
        }

        // ini value checkTask selalu 0 atau false (masih harus diperbaiki)
        holder.binding.checkTask.setOnCheckedChangeListener {
            compoundButton, b ->
            if (compoundButton.isPressed) {
                holder.binding.checkTask.paintFlags = holder.binding.checkTask.paintFlags or STRIKE_THRU_TEXT_FLAG
                holder.binding.checkTask.isEnabled = false
                adapterOnClick(todoList[position])
            }
        }

        holder.binding.imgEdit.setOnClickListener {
            val action = TodoListFragmentDirections.actionEditTodo(todoList[position].uuid)

            Navigation.findNavController(it).navigate(action)
        }
    }

    fun updateTodoList(newTodoList: List<Todo>) {
        todoList.clear()
        todoList.addAll(newTodoList)
        notifyDataSetChanged()
    }
}