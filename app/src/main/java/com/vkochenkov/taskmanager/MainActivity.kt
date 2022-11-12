package com.vkochenkov.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vkochenkov.taskmanager.presentation.AppNavHost
import com.vkochenkov.taskmanager.presentation.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagerTheme {
                AppNavHost()
            }
        }
    }
}