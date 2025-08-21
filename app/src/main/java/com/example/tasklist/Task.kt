package com.example.tasklist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String? = null,
    val time: String = "",
    val description: String? = null,
    val isCompleted: Boolean = false
)