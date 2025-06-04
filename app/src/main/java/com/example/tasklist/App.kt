package com.example.tasklist

import android.app.Application

class App : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
}