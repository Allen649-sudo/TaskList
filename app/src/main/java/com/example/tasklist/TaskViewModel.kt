package com.example.tasklist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TaskViewModel @Inject constructor(private val database: AppDatabase) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> get() = _tasks

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> get() = _selectedTask

    fun insertTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.taskDao().insert(task)
                fetchTasks()
            } catch (e: Exception) {
                Log.e("InsertTaskError", "Couldn't insert task", e)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.taskDao().delete(task)
                fetchTasks()
            } catch (e: Exception) {
                Log.e("DeleteTaskError", "Couldn't delete task", e)
            }
        }


        data class User(val name: String, val age: Int)

        val users = listOf(
            User("Alice", 25),
            User("Bob", 17),
            User("Charlie", 34),
            User("Diana", 15),
            User("Eva", 22)
        )

    }

    fun fetchTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val taskList = database.taskDao().getAllTasks()
                _tasks.value = taskList
            } catch (e: Exception) {
                Log.e("FetchTasksError", "Couldn't fetch tasks", e)
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.taskDao().update(task)
                fetchTasks()
            } catch (e: Exception) {
                Log.e("UpdateTaskError", "Couldn't update task", e)
            }
        }
    }


    fun showEditText(task: Task) {
        viewModelScope.launch {
            try {
                _selectedTask.value = task
            } catch (e: Exception) {
                Log.e("SelectTaskError", "Couldn't select task", e)
            }
        }
    }
}
