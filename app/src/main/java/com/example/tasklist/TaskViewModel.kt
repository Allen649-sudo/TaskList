package com.example.tasklist

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(val dataBase: AppDatabase) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> get() = _tasks
    fun insertTask(task: Task, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val insertedId = dataBase.taskDao().insert(task).toInt() // получаем id только что вставленной задачи
                fetchTask()
                scheduleTaskNotification(context, insertedId, task.title ?: "Задача", task.time)

            } catch (e: Exception) {
                Log.e("FetchPostsError", "Couldn't add task")
            }
        }
    }
    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataBase.taskDao().delete(task)
                fetchTask()
            } catch (e: Exception) {
                Log.e("FetchPostsError", "Couldn't delete task")
            }
        }
    }
    fun fetchTask() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val temporaryTask = dataBase.taskDao().getAllTasks()
                _tasks.value = temporaryTask
            } catch (e: Exception) {
                Log.e("FetchPostsError", "Couldn't fetch tasks")
            }
        }
    }
    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as App).database
                return TaskViewModel(database) as T
            }
        }
    }
}