package com.vkochenkov.taskmanager.presentation.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import com.vkochenkov.taskmanager.data.TasksRepository
import com.vkochenkov.taskmanager.data.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class ShowNotificationReceiver : BroadcastReceiver() {

    val repository: TasksRepository by inject(TasksRepository::class.java)

    companion object {
        const val NOTIFICATION_ID = "NOTIFICATION_ID"
        const val TASK_FOR_NOTIFICATION = "TASK_FOR_NOTIFICATION"
        const val BUNDLE_FOR_NOTIFICATION = "BUNDLE_FOR_NOTIFICATION"
        const val BUNDLE_FROM_NOTIFICATION = "BUNDLE_FROM_NOTIFICATION"

        private const val CHANNEL_ID = "SAMPLE_CHANNEL"
        private const val CHANNEL_NAME = "MY_NOTIFICATION"
    }

    override fun onReceive(context: Context, intent: Intent) {

        val bundle = intent.getBundleExtra(BUNDLE_FOR_NOTIFICATION) ?: return
        val notificationId = bundle.getInt(NOTIFICATION_ID)

        val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getSerializable(TASK_FOR_NOTIFICATION, Task::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getSerializable(TASK_FOR_NOTIFICATION) as Task
        } ?: return

        val intentActivity = Intent(context, MainActivity::class.java)
        intentActivity.putExtra(BUNDLE_FROM_NOTIFICATION, bundle)

        //set unique request code for exact film
        val contentIntent =
            PendingIntent.getActivity(
                context,
                notificationId,
                intentActivity,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

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
            .setContentText(task.description ?: "без описания") //todo fix "" and move to str
            .setContentIntent(contentIntent)
            .setPriority(PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

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