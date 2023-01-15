package com.vkochenkov.taskmanager.presentation.screen.details

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.base.BaseViewModel
import com.vkochenkov.taskmanager.presentation.base.ShowNotificationReceiver
import com.vkochenkov.taskmanager.presentation.navigation.Destination
import com.vkochenkov.taskmanager.presentation.utils.isNotNull
import kotlinx.coroutines.launch
import java.util.*

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: TasksRepository,
    private val applicationContext: Context
) : BaseViewModel() {

    private val taskIdFromNav: String? = savedStateHandle[Destination.Details.argument1]
    private var currentTask: Task? = null

    private var showDialogOnBack: Boolean = false
    private var showDialogOnDelete: Boolean = false
    private var showDialogOnRemoveNotification: Boolean = false

    private var _state: MutableState<DetailsBodyState> =
        mutableStateOf(DetailsBodyState.Loading)
    val state get() = _state

    val onAction = { action: DetailsActions ->
        when (action) {
            is DetailsActions.BackPressed -> onBackPressed(action.showDialog)
            is DetailsActions.TaskChanged -> onTaskChanged(action.task)
            is DetailsActions.SaveTask -> onSaveTask()
            is DetailsActions.CancelBackDialog -> onCancelBackDialog()
            is DetailsActions.CancelRemoveNotificationDialog -> onCancelRemoveNotificationDialog()
            is DetailsActions.CancelDeleteDialog -> onCancelDeleteDialog()
            is DetailsActions.DeleteTask -> onDeleteTask(action.showDialog)
            is DetailsActions.RemoveNotification -> onCanselNotification(action.showDialog)
            is DetailsActions.SetNotificationTime -> onSetNotificationTime(
                year = action.year,
                month = action.month,
                day = action.day,
                hour = action.hour,
                minute = action.minute
            )
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
                    repository.getTask(taskIdFromNav!!.toInt())
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
                showDialogOnBack = false
            )
        }
    }

    private fun onCancelDeleteDialog() {
        currentTask?.let { task ->
            _state.value = DetailsBodyState.Content(
                task = task,
                showDialogOnDelete = false
            )
        }
    }

    private fun onCancelRemoveNotificationDialog() {
        currentTask?.let { task ->
            _state.value = DetailsBodyState.Content(
                task = task,
                showDialogOnRemoveNotification = false
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
                showDialogOnBack = true
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
        showDialog?.let { showDialogOnDelete = it }
        if (showDialogOnDelete && currentTask != null) {
            _state.value = DetailsBodyState.Content(
                task = currentTask!!,
                showDialogOnDelete = true
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

    private fun onCanselNotification(showDialog: Boolean?) {
        showDialog?.let { showDialogOnRemoveNotification = it }
        if (showDialogOnRemoveNotification && currentTask != null) {
            _state.value = DetailsBodyState.Content(
                task = currentTask!!,
                showDialogOnRemoveNotification = true
            )
        } else {
            //todo to think about exeptions?
            viewModelScope.launch {
                runCatching {
                    currentTask = currentTask?.copy(
                        notificationTime = null
                    )
                    //todo add way to remove created alarm
                    currentTask?.let {
                        repository.saveTask(it)
                    }
                }.onFailure {
                    _state.value = DetailsBodyState.Error
                }.onSuccess {
                    _state.value = DetailsBodyState.Content(
                        task = currentTask!!,
                        showDialogOnRemoveNotification = false
                    )
                }
            }
        }
    }

    private fun onSetNotificationTime(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int
    ) {

        val alarmManager =
            applicationContext.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

        //create intent for broadcast receiver
        val broadcastIntent = Intent(applicationContext, ShowNotificationReceiver::class.java)
        broadcastIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)

        //create bundle and set data to it
        val bundle = Bundle()
        //bundle.putParcelable(FILM, film)
        val id = 12
        //createNotificationId(film!!.serverName)
        bundle.putInt(ShowNotificationReceiver.NOTIFICATION_ID, id)
        //broadcastIntent.putExtra(BUNDLE, bundle)

        //create different action for receive all notifications
        broadcastIntent.action = id.toString()

        //create pending intent for alarm manager
        val alarmPendingIntent =
            PendingIntent.getBroadcast(applicationContext, id, broadcastIntent, FLAG_IMMUTABLE)

        //create instance of calendar and set date to it
        val startTime = Calendar.getInstance()
        startTime.set(Calendar.YEAR, year)
        startTime.set(Calendar.MONTH, month)
        startTime.set(Calendar.DAY_OF_MONTH, day)
        startTime.set(Calendar.HOUR_OF_DAY, hour)
        startTime.set(Calendar.MINUTE, minute)
        startTime.set(Calendar.SECOND, 0)

        val alarmStartTime = startTime.timeInMillis

        //set alarm manager for open broadcast receiver after a time
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            alarmStartTime,
            alarmPendingIntent
        )

        currentTask = currentTask?.copy(
            notificationTime = alarmStartTime
        )

        viewModelScope.launch {
            currentTask?.let {
                repository.saveTask(it)
                _state.value = DetailsBodyState.Content(
                    task = it,
                    showDialogOnDelete = false
                )
            }
        }

        //todo to text
        Toast.makeText(
            applicationContext,
            "Notification was created",
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        const val TITLE_MAX_LENGTH = 50
        const val DESCRIPTION_MAX_LENGTH = 300
    }
}