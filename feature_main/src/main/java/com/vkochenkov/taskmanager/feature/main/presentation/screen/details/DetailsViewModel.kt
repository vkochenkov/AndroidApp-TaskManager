package com.vkochenkov.taskmanager.feature.main.presentation.screen.details

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.getBroadcast
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vkochenkov.taskmanager.base.data.StatusPreferences
import com.vkochenkov.taskmanager.base.data.TaskDataService
import com.vkochenkov.taskmanager.base.data.model.Task
import com.vkochenkov.taskmanager.base.presentation.BaseViewModel
import com.vkochenkov.taskmanager.base.presentation.utils.isNotNull
import com.vkochenkov.taskmanager.feature.main.R
import com.vkochenkov.taskmanager.feature.main.presentation.ShowNotificationReceiver
import com.vkochenkov.taskmanager.feature.main.presentation.ShowNotificationReceiver.Companion.BUNDLE_FOR_NOTIFICATION
import com.vkochenkov.taskmanager.feature.main.presentation.ShowNotificationReceiver.Companion.TASK_ID
import com.vkochenkov.taskmanager.feature.main.presentation.navigation.MainDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.*

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val taskDataService: TaskDataService,
    val statusPreferences: StatusPreferences,
    // There is no leak
    private val applicationContext: Context
) : BaseViewModel<DetailsBodyState, DetailsActions>() {

    private val alarmManager =
        applicationContext.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

    private val taskIdFromNav: String? = savedStateHandle[MainDestination.Details.argument1]

    private var currentTask: Task? = null

    private val statuses = statusPreferences.getStatuses()

    private var showDialogOnBack: Boolean = false

    private var _state: MutableStateFlow<DetailsBodyState> =
        MutableStateFlow(DetailsBodyState(currentTask, statuses))
    override val state: StateFlow<DetailsBodyState> get() = _state.asStateFlow()

    override val onAction = { action: DetailsActions ->
        when (action) {
            is DetailsActions.BackPressed -> onBackPressed(action.showDialog)
            is DetailsActions.TaskChanged -> onTaskChanged(action.task)
            is DetailsActions.SaveTask -> onSaveTask()
            is DetailsActions.CancelBackDialog -> onCancelBackDialog()
            is DetailsActions.CancelDeleteDialog -> onCancelDeleteDialog()
            is DetailsActions.DeleteTask -> onDeleteTask(action.showDialog)
            is DetailsActions.CancelNotificationDialog -> onCancelNotificationDialog()
            is DetailsActions.ShowNotificationDialog -> onShowNotificationDialog()
            is DetailsActions.RemoveNotification -> onRemoveNotification()
            is DetailsActions.SetNotification -> onSetNotification(action.time, action.date)
            is DetailsActions.AttachFile -> onAttachFile(action.uri)
            is DetailsActions.OpenAttachment -> onOpenAttachment(action.attachment)
            is DetailsActions.DeleteAttachment -> onDeleteAttachment(action.attachment)
        }
    }

    init {
        getTaskOrCreateNew()
    }

    private fun getTaskOrCreateNew() {
        if (taskIdFromNav.isNotNull()) {
            viewModelScope.launch {
                runCatching {
                    _state.value = _state.value.copy(isLoadingPage = true)
                    taskDataService.getTask(taskIdFromNav!!.toInt())
                        ?: throw NoSuchElementException()
                }.onFailure {
                    _state.value = _state.value.copy(isLoadingPage = false, isErrorPage = true)
                }.onSuccess {
                    currentTask = it
                    _state.value = DetailsBodyState(it, statuses)
                }
            }
        } else {
            showDialogOnBack = true
            currentTask = createNewTask()
            currentTask?.let {
                _state.value = DetailsBodyState(
                    it, statuses
                )
            }
        }
    }

    private fun createNewTask(): Task {
        val currentDate = System.currentTimeMillis().toString()
        return Task(
            title = "New task",
            description = null,
            priority = Task.Priority.NORMAL,
            status = statusPreferences.getStatuses()[0],
            creationDate = currentDate,
            updateDate = currentDate,
            notificationTime = null
        )
    }

    private fun onCancelBackDialog() {
        currentTask?.let { task ->
            _state.value = DetailsBodyState(
                task = task,
                statuses = statuses,
                showOnBackDialog = false
            )
        }
    }

    private fun onCancelDeleteDialog() {
        currentTask?.let { task ->
            _state.value = DetailsBodyState(
                task = task,
                statuses = statuses,
                showOnDeleteDialog = false
            )
        }
    }

    private fun onSaveTask() {
        currentTask?.let { task ->
            if (task.title.isEmpty() || task.title.length > TITLE_MAX_LENGTH) {
                _state.value = DetailsBodyState(
                    task,
                    statuses = statuses,
                    showTitleValidation = true
                )
            } else if (task.description != null && task.description!!.length > DESCRIPTION_MAX_LENGTH) {
                _state.value = DetailsBodyState(
                    task,
                    statuses = statuses,
                    showDescriptionValidation = true
                )
            } else {
                viewModelScope.launch {
                    runCatching {
                        taskDataService.saveTask(task)
                    }.onFailure {
                        _state.value = _state.value.copy(isErrorPage = true)
                    }.onSuccess {
                        _state.value = DetailsBodyState(task, statuses)
                        navController.popBackStack()
                    }
                }
            }
        }
    }

    private fun onBackPressed(showDialog: Boolean?) {
        showDialog?.let { showDialogOnBack = it }
        if (showDialogOnBack && currentTask != null) {
            _state.value = DetailsBodyState(
                task = currentTask!!,
                statuses = statuses,
                showOnBackDialog = true
            )
        } else {
            navController.popBackStack()
        }
    }

    private fun onTaskChanged(task: Task) {
        currentTask = task
        _state.value = DetailsBodyState(task, statuses)
        showDialogOnBack = true
    }

    private fun onDeleteTask(showDialog: Boolean?) {
        if (showDialog == true && currentTask != null) {
            _state.value = DetailsBodyState(
                task = currentTask!!,
                statuses = statuses,
                showOnDeleteDialog = true
            )
        } else {
            // cancel notification if exists
            alarmManager.cancel(getAlarmPendingIntent())

            // delete attachments if exists
            val attachments = currentTask?.attachments
            attachments?.forEach {
                try {
                    val file = File(applicationContext.filesDir, it)
                    file.delete()
                } catch (ex: Exception) {
                    // do nothing
                }
            }

            viewModelScope.launch {
                runCatching {
                    currentTask?.let {
                        taskDataService.deleteTask(it)
                    }
                }.onFailure {
                    _state.value = _state.value.copy(isErrorPage = true)
                }.onSuccess {
                    navController.popBackStack()
                }
            }
        }
    }

    private fun onAttachFile(uri: Uri) {
        try {
            // from https://stackoverflow.com/questions/12473851/how-i-can-get-the-mime-type-of-a-file-having-its-uri
            val extensionType = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
                MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(applicationContext.contentResolver.getType(uri))
            } else {
                MimeTypeMap.getFileExtensionFromUrl(
                    Uri.fromFile(uri.path?.let { File(it) }).toString()
                )
            }

            val inputBytes = applicationContext.contentResolver.openInputStream(uri)?.use {
                it.readBytes()
            }

            val ending = if (extensionType != null) {
                ".$extensionType"
            } else ""
            val fileName = "attached_file_${System.currentTimeMillis()}" + ending
            val file = File(applicationContext.filesDir, fileName)

            FileOutputStream(file).use { stream ->
                inputBytes?.let { bytes ->
                    stream.write(bytes)
                }
            }

            viewModelScope.launch {
                runCatching {
                    currentTask = currentTask?.copy(
                        attachments = currentTask?.attachments?.plus(listOf(fileName)) ?: listOf(
                            fileName
                        )
                    )
                    currentTask?.let {
                        taskDataService.saveTask(it)
                    }
                }.onFailure {
                    _state.value = _state.value.copy(isErrorPage = true)
                }.onSuccess {
                    _state.value = _state.value.copy(
                        task = currentTask
                    )
                    showDialogOnBack = false
                }
            }
        } catch (ex: Exception) {
            showToast(R.string.toast_ex)
        }
    }

    private fun onOpenAttachment(attachment: String) {
        try {
            val file = File(applicationContext.filesDir, attachment)

            val i = Intent(
                Intent.ACTION_VIEW,
                FileProvider.getUriForFile(
                    applicationContext,
                    applicationContext.packageName + ".provider", // same as in manifest
                    file
                )
            )

            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            i.addFlags(FLAG_ACTIVITY_NEW_TASK)

            applicationContext.startActivity(i)

        } catch (ex: Exception) {
            showToast(R.string.toast_no_app_for_type)
        }
    }

    private fun onDeleteAttachment(attachment: String) {
        try {
            val file = File(applicationContext.filesDir, attachment)
            file.delete()
            val attachments = (currentTask?.attachments ?: listOf()).toMutableList()
            attachments.remove(attachment)
            viewModelScope.launch {
                runCatching {
                    currentTask = currentTask?.copy(
                        attachments = attachments
                    )
                    currentTask?.let {
                        taskDataService.saveTask(it)
                    }
                }.onFailure {
                    _state.value = _state.value.copy(isErrorPage = true)
                }.onSuccess {
                    _state.value = _state.value.copy(
                        task = currentTask
                    )
                    showDialogOnBack = false
                }
            }
        } catch (ex: Exception) {
            showToast(R.string.toast_cant_delete_file)
        }
    }

    private fun onCancelNotificationDialog() {
        currentTask?.let { task ->
            _state.value = DetailsBodyState(
                task = task,
                statuses = statuses,
                showNotificationDialog = false
            )
        }
    }

    private fun onShowNotificationDialog() {
        currentTask?.let { task ->
            _state.value = DetailsBodyState(
                task = task,
                statuses = statuses,
                showNotificationDialog = true
            )
        }
    }

    private fun onRemoveNotification() {
        viewModelScope.launch {
            runCatching {
                currentTask = currentTask?.copy(
                    notificationTime = null
                )
                currentTask?.let {
                    taskDataService.saveTask(it)
                }
            }.onFailure {
                _state.value = _state.value.copy(isErrorPage = true)
            }.onSuccess {
                _state.value = DetailsBodyState(
                    task = currentTask!!,
                    statuses = statuses,
                    showNotificationDialog = false
                )
                showDialogOnBack = false
            }
        }

        alarmManager.cancel(getAlarmPendingIntent())

        showToast(R.string.toast_remove_notification)
    }

    private fun onSetNotification(
        time: String,
        date: String
    ) {
        val calendarForNotification = Calendar.getInstance()

        when {
            time.isEmpty() -> {
                showToast(R.string.toast_empty_time)
                return
            }
            date.isEmpty() -> {
                showToast(R.string.toast_empty_date)
                return
            }
            !time.matches("[0-2][0-9]:[0-5][0-9]".toRegex()) -> {
                showToast(R.string.toast_wrong_time)
                return
            }
            !date.matches("[0-3][0-9].[0-1][0-9].[0-9][0-9][0-9][0-9]".toRegex()) -> {
                showToast(R.string.toast_wrong_date)
                return
            }
        }

        try {
            val t = time.split(":")
            val d = date.split(".")

            val nYear = d[2].toInt()
            val nMonth = d[1].toInt() - 1 // calendar's month starts from 0
            val nDay = d[0].toInt()
            val nHour = t[0].toInt()
            val nMinute = t[1].toInt()

            calendarForNotification.set(Calendar.YEAR, nYear)
            calendarForNotification.set(Calendar.MONTH, nMonth)
            calendarForNotification.set(Calendar.DAY_OF_MONTH, nDay)
            calendarForNotification.set(Calendar.HOUR_OF_DAY, nHour)
            calendarForNotification.set(Calendar.MINUTE, nMinute)
            calendarForNotification.set(Calendar.SECOND, 0)

        } catch (ex: Exception) {
            showToast(R.string.toast_ex)
            return
        }

        val alarmStartTime = calendarForNotification.timeInMillis

        if (alarmStartTime < System.currentTimeMillis()) {
            showToast(R.string.toast_past)
            return
        }

        // cancel previous if exists and set new
        val pendingIntent = getAlarmPendingIntent()
        alarmManager.cancel(pendingIntent)
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            alarmStartTime,
            pendingIntent
        )

        currentTask = currentTask?.copy(
            notificationTime = alarmStartTime
        )

        viewModelScope.launch {
            currentTask?.let { task ->
                runCatching {
                    taskDataService.saveTask(task)
                }.onSuccess {
                    showDialogOnBack = false
                    _state.value = DetailsBodyState(
                        task = task,
                        statuses = statuses,
                        showNotificationDialog = false
                    )
                }.onFailure {
                    _state.value = _state.value.copy(isErrorPage = true)
                }
            }
        }

        showToast(R.string.toast_set_notification)
    }

    private fun getAlarmPendingIntent(): PendingIntent {
        val taskId = currentTask?.id ?: 0

        val broadcastIntent = Intent(applicationContext, ShowNotificationReceiver::class.java)
        broadcastIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        //create different action for receive notifications from all tasks
        broadcastIntent.action = taskId.toString()

        broadcastIntent.putExtra(
            BUNDLE_FOR_NOTIFICATION,
            Bundle().apply {
                putInt(TASK_ID, taskId)
            }
        )

        return getBroadcast(
            applicationContext,
            taskId, // requestCode
            broadcastIntent,
            FLAG_IMMUTABLE
        )
    }

    private fun showToast(stringRes: Int) {
        applicationContext.let {
            Toast.makeText(it, it.getString(stringRes), Toast.LENGTH_LONG)
                .show()
        }
    }

    companion object {
        private const val TITLE_MAX_LENGTH = 50
        private const val DESCRIPTION_MAX_LENGTH = 300
    }
}