package com.vkochenkov.taskmanager.presentation.screen.details

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vkochenkov.taskmanager.R
import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.base.BaseViewModel
import com.vkochenkov.taskmanager.presentation.base.ShowNotificationReceiver
import com.vkochenkov.taskmanager.presentation.base.ShowNotificationReceiver.Companion.BUNDLE_FOR_NOTIFICATION
import com.vkochenkov.taskmanager.presentation.base.ShowNotificationReceiver.Companion.TASK_ID
import com.vkochenkov.taskmanager.presentation.navigation.Destination
import com.vkochenkov.taskmanager.presentation.utils.isNotNull
import kotlinx.coroutines.launch
import java.util.*
import kotlin.NoSuchElementException

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: TasksRepository,
    private val applicationContext: Context
) : BaseViewModel() {

    private val taskIdFromNav: String? = savedStateHandle[Destination.Details.argument1]

    // todo to think about subscription on DB
    private var currentTask: Task? = null

    private var showDialogOnBack: Boolean = false

    private var _state: MutableState<DetailsBodyState> =
        mutableStateOf(DetailsBodyState.Loading)
    val state get() = _state

    val onAction = { action: DetailsActions ->
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
            is DetailsActions.SetNotification -> onSetNotification(action.date)
        }
    }

    init {
        getTaskOrCreateNew()
    }

    private fun getTaskOrCreateNew() {
        if (taskIdFromNav.isNotNull()) {
            viewModelScope.launch {
                runCatching {
                    _state.value = DetailsBodyState.Loading
                    repository.getTask(taskIdFromNav!!.toInt()) ?: throw NoSuchElementException()
                }.onFailure {
                    _state.value = DetailsBodyState.Error
                }.onSuccess {
                    currentTask = it
                    _state.value = DetailsBodyState.Content(it)
                }
            }
        } else {
            showDialogOnBack = true
            currentTask = repository.getNewTaskSample()
            currentTask?.let {
                _state.value = DetailsBodyState.Content(
                    it
                )
            }
        }
    }

    private fun onCancelBackDialog() {
        currentTask?.let { task ->
            _state.value = DetailsBodyState.Content(
                task = task,
                showOnBackDialog = false
            )
        }
    }

    private fun onCancelDeleteDialog() {
        currentTask?.let { task ->
            _state.value = DetailsBodyState.Content(
                task = task,
                showOnDeleteDialog = false
            )
        }
    }

    private fun onSaveTask() {
        currentTask?.let { task ->
            if (task.title.isEmpty() || task.title.length > TITLE_MAX_LENGTH) {
                _state.value = DetailsBodyState.Content(task, showTitleValidation = true)
            } else if (task.description != null && task.description.length > DESCRIPTION_MAX_LENGTH) {
                _state.value = DetailsBodyState.Content(task, showDescriptionValidation = true)
            } else {
                viewModelScope.launch {
                    runCatching {
                        repository.saveTask(task)
                    }.onFailure {
                        _state.value = DetailsBodyState.Error
                    }.onSuccess {
                        _state.value = DetailsBodyState.Content(task)
                        navController.popBackStack()
                    }
                }
            }
        }
    }

    private fun onBackPressed(showDialog: Boolean?) {
        showDialog?.let { showDialogOnBack = it }
        if (showDialogOnBack && currentTask != null) {
            _state.value = DetailsBodyState.Content(
                task = currentTask!!,
                showOnBackDialog = true
            )
        } else {
            navController.popBackStack()
        }
    }

    private fun onTaskChanged(task: Task) {
        currentTask = task
        _state.value = DetailsBodyState.Content(task)
        showDialogOnBack = true
    }

    private fun onDeleteTask(showDialog: Boolean?) {
        if (showDialog == true && currentTask != null) {
            _state.value = DetailsBodyState.Content(
                task = currentTask!!,
                showOnDeleteDialog = true
            )
        } else {
            viewModelScope.launch {
                runCatching {
                    currentTask?.let {
                        repository.deleteTask(it)
                    }
                }.onFailure {
                    _state.value = DetailsBodyState.Error
                }.onSuccess {
                    navController.popBackStack()
                }
            }
        }
    }

    private fun onCancelNotificationDialog() {
        currentTask?.let { task ->
            _state.value = DetailsBodyState.Content(
                task = task,
                showNotificationDialog = false
            )
        }
    }

    private fun onShowNotificationDialog() {
        currentTask?.let { task ->
            _state.value = DetailsBodyState.Content(
                task = task,
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
                    repository.saveTask(it)
                }
            }.onFailure {
                _state.value = DetailsBodyState.Error
            }.onSuccess {
                _state.value = DetailsBodyState.Content(
                    task = currentTask!!,
                    showNotificationDialog = false
                )
                showDialogOnBack = false
            }
        }

        val alarmManager =
            applicationContext.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(getAlarmPendingIntent())

        applicationContext.let {
            Toast.makeText(it, it.getString(R.string.toast_remove_notification), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun onSetNotification(
        date: Long
    ) {

        //create instance of calendar and set date to it
        val startTime = Calendar.getInstance()

        // todo after create data picker
        var year: Int = startTime.get(Calendar.YEAR)
        var month: Int = startTime.get(Calendar.MONTH)
        var day: Int = startTime.get(Calendar.DAY_OF_MONTH)
        var hour: Int = startTime.get(Calendar.HOUR_OF_DAY)
        var minute: Int = startTime.get(Calendar.MINUTE)

        startTime.set(Calendar.YEAR, year)
        startTime.set(Calendar.MONTH, month)
        startTime.set(Calendar.DAY_OF_MONTH, day)
        startTime.set(Calendar.HOUR_OF_DAY, hour)
        startTime.set(Calendar.MINUTE, minute)
        startTime.set(Calendar.SECOND, 0)

        val alarmStartTime = startTime.timeInMillis + 10_000

        val alarmManager =
            applicationContext.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

        //cancel previous if exists and set new
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
                    repository.saveTask(task)
                }.onSuccess {
                    showDialogOnBack = false
                    _state.value = DetailsBodyState.Content(
                        task = task,
                        showNotificationDialog = false
                    )
                }.onFailure {
                    _state.value = DetailsBodyState.Error
                }
            }
        }

        applicationContext.let {
            Toast.makeText(it, it.getString(R.string.toast_set_notification), Toast.LENGTH_LONG)
                .show()
        }
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

    companion object {
        private const val TITLE_MAX_LENGTH = 50
        private const val DESCRIPTION_MAX_LENGTH = 300
    }
}