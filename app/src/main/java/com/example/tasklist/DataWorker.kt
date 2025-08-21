package com.example.tasklist

import android.content.Context
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddTaskWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val title = inputData.getString("taskTitle")
        if (title != null) {
            val database = AppDatabase.getDatabase(applicationContext)
            val taskDao = database.taskDao()
            try {
                taskDao.insert(Task(title = title))
                return Result.success()
            } catch (e: Exception) {
                return Result.failure()
            }
        }
        return Result.failure()
    }
}