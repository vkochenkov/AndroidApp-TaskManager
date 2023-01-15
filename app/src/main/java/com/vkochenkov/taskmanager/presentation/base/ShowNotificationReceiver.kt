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


class ShowNotificationReceiver : BroadcastReceiver() {

    companion object {
        val CHANNEL_ID = "SAMPLE_CHANNEL"
        val CHANNEL_NAME = "MY_NOTIFICATION"
        val NOTIFICATION_ID = "NOTIFICATION_ID"
    }

    override fun onReceive(context: Context, intent: Intent) {

    //    val bundle = intent.getBundleExtra(BUNDLE)
        val notificationId = 123
      //  val film = bundle?.getParcelable<Film>(FILM)

        val intentActivity = Intent(context, MainActivity::class.java)
      //  intentActivity.putExtra(BUNDLE, bundle)
        //set unique request code for exact film
        val contentIntent =
            PendingIntent.getActivity(context, notificationId, intentActivity, PendingIntent.FLAG_UPDATE_CURRENT)

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
            .setContentTitle("context.getString(R.string.notification_title_str)")
            .setContentText("film.title")
            .setContentIntent(contentIntent)
            .setPriority(PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)

        //todo remove notification from DB
    }
}