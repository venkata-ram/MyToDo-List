package com.buildappswithvenkat.mytodoapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.buildappswithvenkat.mytodoapp.data.TodoDatabase
import com.buildappswithvenkat.mytodoapp.data.models.TodoData
import com.buildappswithvenkat.mytodoapp.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    private val toDoDao = TodoDatabase.getDataBase(application).toDoDao()
    private val repository : ToDoRepository

    val getAllData : LiveData<List<TodoData>>
    init {
        repository = ToDoRepository(toDoDao)
        getAllData = repository.getAllData
    }

    fun insertData(todoData: TodoData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(todoData)
        }
    }

    fun updateData(todoData: TodoData){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateData(todoData)
        }
    }

    fun deleteItem(todoData: TodoData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(todoData)
        }
    }

}