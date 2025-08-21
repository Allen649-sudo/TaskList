package com.example.tasklist

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

fun scheduleTaskNotification(context: Context, taskId: Int, title: String, time: String) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("taskTitle", title)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        taskId, // уникальный requestCode для каждой задачи
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Парсим время задачи (например "15:30")
    val parts = time.split(":")
    if (parts.size != 2) return
    val hour = parts[0].toIntOrNull() ?: return
    val minute = parts[1].toIntOrNull() ?: return

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        if (before(Calendar.getInstance())) {
            add(Calendar.DATE, 1) // если время уже прошло сегодня
        }
    }

    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
}
