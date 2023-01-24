package com.vkochenkov.taskmanager.presentation.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import androidx.core.net.toUri
import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.data.model.Task
import com.vkochenkov.taskmanager.presentation.navigation.Destination
import com.vkochenkov.taskmanager.presentation.navigation.buildDeeplink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.inject
import java.util.*

class ShowNotificationReceiver : BroadcastReceiver() {

    private val repository: TasksRepository by inject(TasksRepository::class.java)

    companion object {
        const val TASK_ID = "TASK_ID"
        const val BUNDLE_FOR_NOTIFICATION = "BUNDLE_FOR_NOTIFICATION"

        private const val CHANNEL_ID = "SAMPLE_CHANNEL"
        private const val CHANNEL_NAME = "MY_NOTIFICATION"
    }

    override fun onReceive(context: Context, intent: Intent) {

        val bundle = intent.getBundleExtra(BUNDLE_FOR_NOTIFICATION) ?: return
        val taskId = bundle.getInt(TASK_ID)

        Log.d("vladd", "ShowNotificationReceiver taskId = $taskId")

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
            Intent.ACTION_VIEW,
            buildDeeplink(Destination.Details.passArguments(taskId.toString())).toUri()
        )

        val contentIntent: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intentActivity)
            getPendingIntent(taskId, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //for api 26 and above
            val channelName = CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)

            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setChannelId(CHANNEL_ID)
            .setSmallIcon(androidx.loader.R.drawable.notification_bg)
            .setContentTitle(task.title)
            .setContentText(task.description)
            .setContentIntent(contentIntent)
            .setPriority(PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationId = (Calendar.getInstance().timeInMillis / 1000).toInt()
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