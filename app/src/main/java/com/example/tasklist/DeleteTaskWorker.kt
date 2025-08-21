package com.example.tasklist

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DeleteTaskWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val id = inputData.getInt("taskId", 0) // Получаем id задачи из inputData
        if (id != 0) {
            val database = AppDatabase.getDatabase(applicationContext) // Получаем базу данных
            val taskDao = database.taskDao() // Получаем DAO
            return try {
                taskDao.delete(Task(id = id)) // Удаляем задачу
                Result.success() // Успешное выполнение
            } catch (e: Exception) {
                Result.failure() // Ошибка выполнения
            }
        }
        return Result.failure() // Если id был 0
    }
}
