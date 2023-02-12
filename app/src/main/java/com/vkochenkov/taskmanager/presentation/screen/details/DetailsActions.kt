package com.vkochenkov.taskmanager.presentation.screen.details

import android.net.Uri
import com.vkochenkov.taskmanager.data.model.Task

sealed class DetailsActions {

    data class TaskChanged(val task: Task) : DetailsActions()

    data class BackPressed(val showDialog: Boolean? = null) : DetailsActions()

    object SaveTask : DetailsActions()

    data class DeleteTask(val showDialog: Boolean? = null) : DetailsActions()

    object CancelBackDialog : DetailsActions()

    object CancelDeleteDialog : DetailsActions()

    object ShowNotificationDialog : DetailsActions()

    object CancelNotificationDialog : DetailsActions()

    data class SetNotification(
        val time: String,
        val date: String
    ) : DetailsActions()

    object RemoveNotification : DetailsActions()

    data class AttachFile(val uri: Uri) : DetailsActions()

    data class OpenAttachment(val attachment: String) : DetailsActions()

    data class DeleteAttachment(val attachment: String) : DetailsActions()
}
