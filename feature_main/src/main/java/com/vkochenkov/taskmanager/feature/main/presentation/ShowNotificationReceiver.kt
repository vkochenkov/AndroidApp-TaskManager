package com.vkochenkov.taskmanager.feature.main.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import androidx.core.net.toUri
import com.vkochenkov.taskmanager.base.data.TaskDataService
import com.vkochenkov.taskmanager.base.data.model.Task
import com.vkochenkov.taskmanager.base.presentation.navigation.Route.Companion.buildDeeplink
import com.vkochenkov.taskmanager.feature.main.R
import com.vkochenkov.taskmanager.feature.main.presentation.navigation.MainDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.inject
import java.util.*

class ShowNotificationReceiver : BroadcastReceiver() {

    private val repository: TaskDataService by inject(TaskDataService::class.java)

    companion object {
        const val TASK_ID = "TASK_ID"
        const val BUNDLE_FOR_NOTIFICATION = "BUNDLE_FOR_NOTIFICATION"

        private const val CHANNEL_ID = "SAMPLE_CHANNEL"
        private const val CHANNEL_NAME = "MY_NOTIFICATION"
    }

    override fun onReceive(context: Context, intent: Intent) {

        val bundle = intent.getBundleExtra(BUNDLE_FOR_NOTIFICATION) ?: return
        val taskId = bundle.getInt(TASK_ID)

        val task: Task = runBlocking {
            repository.getTask(taskId)
        } ?: return

        CoroutineScope(SupervisorJob()).launch {
            repository.saveTask(
                task.copy(
                    notificationTime = null
                )
            )
        }

        val intentActivity = Intent(
            "com.vkochenkov.taskmanager.OPEN_TASK",
            buildDeeplink(MainDestination.Details.passArguments(taskId.toString())).toUri()
        )

        val contentIntent: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intentActivity)
            getPendingIntent(taskId, PendingIntent.FLAG_IMMUTABLE)
        }

        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // for api 26 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)

            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setChannelId(CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setContentTitle(task.title)
            .setContentText(task.description)
            .setContentIntent(contentIntent)
            .setPriority(PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationId = (Calendar.getInstance().timeInMillis / 1000).toInt() // create uniq id
        notificationManager.notify(notificationId, notification)

        CoroutineScope(SupervisorJob()).launch {
            repository.saveTask(
                task.copy(
                    notificationTime = null
                )
            )
        }
    }
}