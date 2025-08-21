package com.example.tasklist

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.Calendar

@Composable
fun TaskScreen() {
    val viewModel: TaskViewModel = viewModel(factory = TaskViewModel.factory)
    var searchQuery by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    val context = LocalContext.current // Объявляем контекст

    Column(modifier = Modifier.fillMaxSize())  {
        Spacer(modifier = Modifier.height(12.dp)) // Отступ перед списком задач
        // Box для наложения кнопки на TextField
        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Добавить задачу") },
                modifier = Modifier.fillMaxWidth() // Заполняет всю информацию
            )

            // Кнопка, наложенная на TextField
            Button(
                onClick = {
                    if (searchQuery.isNotBlank()) {
                        viewModel.insertTask(Task(title = searchQuery, time = selectedTime), context)
                    }
//                    scheduleNotification()
                    searchQuery = ""
                    selectedTime = ""
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd) // Центрировать по вертикали и прислонить к правой стороне
                    .padding(start = 8.dp) // Добавляем отступ слева, чтобы кнопка не была прижата к текстовому полю
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Добавить")
            }
        }
        Button(onClick = {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(context, { _, selectedHour, selectedMinute ->
                selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            }, hour, minute, true).show()
        }) {
            Text(text = if (selectedTime.isEmpty()) "Выберите время" else selectedTime)
        }
        Spacer(modifier = Modifier.height(16.dp)) // Отступ перед списком задач
        // Отображение задач
        TaskCardScreen(viewModel)
    }
}
