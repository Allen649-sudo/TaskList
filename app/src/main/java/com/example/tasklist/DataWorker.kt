package com.example.tasklist

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class AddTaskWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val title = inputData.getString("taskTitle") // Получаем данные задачи из inputData
        if (title != null) {
            val database = AppDatabase.getDatabase(applicationContext) // Получаем базу данных
            val taskDao = database.taskDao() // Получаем DAO

            return try {
                taskDao.insert(Task(title = title)) // Вставляем новую задачу
                Result.success() // Успешное выполнение
            } catch (e: Exception) {
                Result.failure() // Ошибка выполнения
            }
        }
        return Result.failure() // Если title был null
    }
}