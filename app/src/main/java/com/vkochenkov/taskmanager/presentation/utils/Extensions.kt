package com.vkochenkov.taskmanager.presentation.utils

import androidx.compose.ui.graphics.Color
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.theme.ColorPriorityHigh
import com.vkochenkov.taskmanager.presentation.theme.ColorPriorityLow
import com.vkochenkov.taskmanager.presentation.theme.ColorPriorityNormal
import java.util.*

fun String?.isNotNull(): Boolean {
    return this != null && this != "null"
}

fun Task.Priority.getColor(): Color {
    return when (this) {
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

fun Task.Priority.getNameForUi(): String {
    return this.toString().lowercase()
        .replaceFirstChar { it.titlecase(Locale.ROOT) }
}

// hh:mm
fun Task.getFormattedNotificationTime(): String {
    notificationTime?.let {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = notificationTime
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calendar.get(Calendar.MINUTE)
        return "${zeroPresence(hour)}:${zeroPresence(minute)}"
    } ?: return ""
}

// dd.mm.yyyy
fun Task.getFormattedNotificationDate(): String {
    notificationTime?.let {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = notificationTime
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH) + 1
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        return "${zeroPresence(day)}.${zeroPresence(month)}.$year"
    } ?: return ""
}

private fun zeroPresence(int: Int): String {
    return if (int < 10) {
        "0$int"
    } else {
        int.toString()
    }
}
