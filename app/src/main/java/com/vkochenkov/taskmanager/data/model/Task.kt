package com.vkochenkov.taskmanager.data.model

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vkochenkov.taskmanager.R
import com.vkochenkov.taskmanager.presentation.theme.ColorPriorityHigh
import com.vkochenkov.taskmanager.presentation.theme.ColorPriorityLow
import com.vkochenkov.taskmanager.presentation.theme.ColorPriorityNormal

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String?,
    val priority: Priority,
    val status: Status
    // todo add update date, and should filter in column by date and priority
) {

    enum class Priority {
        LOW, NORMAL, HIGH;

        fun getColor(): Color {
            return when (this) {
                LOW -> {
                    ColorPriorityLow
                }
                NORMAL -> {
                    ColorPriorityNormal
                }
                HIGH -> {
                    ColorPriorityHigh
                }
            }
        }
    }

    enum class Status {

        TO_DO,
        IN_PROGRESS,
        REVIEW,
        DONE;

        fun getNameForUi(context: Context): String {
            return when (this) {
                TO_DO -> {
                    context.getString(R.string.status_to_do)
                }
                IN_PROGRESS -> {
                    context.getString(R.string.status_in_progress)
                }
                REVIEW -> {
                    context.getString(R.string.status_review)
                }
                DONE -> {
                    context.getString(R.string.status_done)
                }
            }
        }
    }
}
