package com.vkochenkov.taskmanager.presentation.utils

import androidx.compose.ui.graphics.Color
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.theme.ColorPriorityHigh
import com.vkochenkov.taskmanager.presentation.theme.ColorPriorityLow
import com.vkochenkov.taskmanager.presentation.theme.ColorPriorityNormal

fun getPriorityColor(priority: Task.Priority): Color {
    return when (priority) {
        Task.Priority.LOW -> {
            ColorPriorityLow
        }
        Task.Priority.NORMAL -> {
            ColorPriorityNormal
        }
        Task.Priority.HIGH -> {
            ColorPriorityHigh
        }
    }
}