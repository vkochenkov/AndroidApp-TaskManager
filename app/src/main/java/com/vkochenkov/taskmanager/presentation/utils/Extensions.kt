package com.vkochenkov.taskmanager.presentation.utils

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.vkochenkov.taskmanager.R
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.theme.ColorPriorityHigh
import com.vkochenkov.taskmanager.presentation.theme.ColorPriorityLow
import com.vkochenkov.taskmanager.presentation.theme.ColorPriorityNormal

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
