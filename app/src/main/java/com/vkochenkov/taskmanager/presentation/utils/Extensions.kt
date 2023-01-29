package com.vkochenkov.taskmanager.presentation.utils

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.vkochenkov.taskmanager.R
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

fun Task.Status.getNameForUi(context: Context): String {
    return when (this) {
        Task.Status.TO_DO -> {
            context.getString(R.string.status_to_do)
        }
        Task.Status.IN_PROGRESS -> {
            context.getString(R.string.status_in_progress)
        }
        Task.Status.REVIEW -> {
            context.getString(R.string.status_review)
        }
        Task.Status.DONE -> {
            context.getString(R.string.status_done)
        }
    }
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
