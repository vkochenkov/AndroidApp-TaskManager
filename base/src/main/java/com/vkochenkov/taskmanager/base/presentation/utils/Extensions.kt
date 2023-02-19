package com.vkochenkov.taskmanager.base.presentation.utils

import androidx.compose.ui.graphics.Color
import com.vkochenkov.taskmanager.base.data.model.Task
import com.vkochenkov.taskmanager.base.presentation.theme.ColorPriorityHigh
import com.vkochenkov.taskmanager.base.presentation.theme.ColorPriorityLow
import com.vkochenkov.taskmanager.base.presentation.theme.ColorPriorityNormal
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

fun Task.getFormattedNotificationTime(): String {
    return notificationTime?.getFormattedTime() ?: return ""
}

fun Task.getFormattedNotificationDate(): String {
    return notificationTime?.getFormattedDate() ?: ""
}

// dd.mm.yyyy
fun Long.getFormattedDate(): String {
    this.let {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH) + 1
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        return "${zeroPresence(day)}.${zeroPresence(month)}.$year"
    }
}

// hh:mm
fun Long.getFormattedTime(): String {
    this.let {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calendar.get(Calendar.MINUTE)
        return "${zeroPresence(hour)}:${zeroPresence(minute)}"
    }
}

private fun zeroPresence(int: Int): String {
    return if (int < 10) {
        "0$int"
    } else {
        int.toString()
    }
}
