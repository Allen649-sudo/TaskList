package com.example.tasklist

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TaskCardScreen(viewModel: TaskViewModel = viewModel(factory = TaskViewModel.factory)) {
    val tasks by viewModel.tasks.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTask()
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        items(tasks) { task ->
            Card(
                modifier = Modifier
                    .fillMaxWidth() // Используем fillMaxWidth, чтобы карточка занимала всю ширину
                    .shadow(5.dp, RoundedCornerShape(24.dp))
                    .border(1.dp, Color(0xFFFFEBF2), RoundedCornerShape(24.dp)),
                backgroundColor = Color(0xFFFFE4ED),
                shape = RoundedCornerShape(20.dp),
            ) {
                Row(
                    modifier = Modifier.padding(8.dp), // Отступы внутри карточки
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically // Центрируем по вертикали
                ) {
                    Column(
                        modifier = Modifier.weight(1f), // Занимает оставшееся пространство
                    ) {
                        Text(
                            text = "${task.title}",
                            fontSize = 20.sp,
                            maxLines = 2, // Ограничиваем текст одной линией
                            overflow = TextOverflow.Ellipsis // Добавляем многоточие, если текст длиннее
                        )

                        Text(
                            text = task.time,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp)) // Отступ между текстом и кнопкой
                    Button(
                        onClick = {
                            viewModel.deleteTask(task) // Обработка нажатия кнопки
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Удалить задачу", tint = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp)) // Отступ между карточками
        }
        if (tasks.isEmpty()) {
            item { Text("Нет задач для отображения.") } // Показать текст, если задач нет
        }
    }
}