package com.example.tasklist

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TaskScreen() {
    val viewModel: TaskViewModel = viewModel(factory = TaskViewModel.factory)
    var searchQuery by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize()) {
        // Box для наложения кнопки на TextField
        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Добавить задачу") },
                modifier = Modifier.fillMaxWidth() // Заполняет всю ширину
            )
            // Кнопка, наложенная на TextField
            Button(
                onClick = {
                    if (searchQuery.isNotBlank()) {
                        val newTask = Task(title = searchQuery) // Создаем новую задачу
                        viewModel.insertTask(newTask)
                        searchQuery = "" // Очищаем поле ввода
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd) // Центрировать по вертикали и прислонить к правой стороне
                    .padding(start = 8.dp) // Добавляем отступ слева, чтобы кнопка не была прижата к текстовому полю
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Добавить")
            }
        }
        Spacer(modifier = Modifier.height(16.dp)) // Отступ перед списком задач
        // Отображение задач
        TaskCardScreen()
    }
}