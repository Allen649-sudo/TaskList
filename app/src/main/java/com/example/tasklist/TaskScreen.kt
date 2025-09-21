package com.example.tasklist

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import java.util.Calendar

@Composable
fun TaskScreen(
    viewModel: TaskViewModel,
    onEditText: (Task) -> Unit,
){

    var taskToDelete by remember { mutableStateOf<Task?>(null) }
    var playAnimation by remember { mutableStateOf(false) }

    val frames = listOf(
        R.drawable.anim_delete_0,
        R.drawable.anim_delete_1,
        R.drawable.anim_delete_2,
        R.drawable.anim_delete_3,
        R.drawable.anim_delete_4,
        R.drawable.anim_delete_5,
        R.drawable.anim_delete_6
    )

    var searchQuery by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    val context = LocalContext.current // Объявляем контекст
    if (playAnimation) {
        SpriteAnimation(
            frames = frames,
            onAnimationEnd = {
                taskToDelete?.let { viewModel.deleteTask(it) }
                taskToDelete = null
                playAnimation = false
            }
        )
    }
    Column(modifier = Modifier.fillMaxSize())  {
        Spacer(modifier = Modifier.height(12.dp)) // Отступ перед списком задач
        // Box для наложения кнопки на TextField
        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Добавить задачу") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                )

            // Кнопка, наложенная на TextField
            Button(
                onClick = {
                    if (searchQuery.isNotBlank()) {
                        viewModel.insertTask(Task(title = searchQuery, time = selectedTime))
                    }
//                    scheduleNotification()
                    searchQuery = ""
                    selectedTime = ""
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(start = 8.dp),
                shape = RoundedCornerShape(12.dp),
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
        },
        shape = RoundedCornerShape(12.dp),
        ) {
            Text(text = if (selectedTime.isEmpty()) "Выберите время" else selectedTime)
        }
        Spacer(modifier = Modifier.height(12.dp)) // Отступ перед списком задач
        // Отображение задач
        TaskCardScreen(
            viewModel = viewModel,
            onEditText = onEditText,
            onRequestDelete = { task ->
                taskToDelete = task
                playAnimation = true
            }
        )


    }
}
