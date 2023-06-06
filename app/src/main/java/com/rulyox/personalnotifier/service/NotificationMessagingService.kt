package com.rulyox.personalnotifier.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rulyox.personalnotifier.CHANNEL_ID
import com.rulyox.personalnotifier.INTENT_UPDATE
import com.rulyox.personalnotifier.LOG_TAG
import com.rulyox.personalnotifier.PREFS_NAME
import com.rulyox.personalnotifier.R
import com.rulyox.personalnotifier.data.Notification
import com.rulyox.personalnotifier.data.NotificationStore
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date

class NotificationMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(LOG_TAG, token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notification = Notification(
            timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
            title = message.data["title"].toString(),
            body = message.data["body"].toString()
        )
        Log.d(LOG_TAG, Json.encodeToString(notification))
        handleNewNotification(notification)
    }

    private fun handleNewNotification(notification: Notification) {
        val prefs: SharedPreferences = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        NotificationStore.addNotification(prefs, notification)
        val notificationCount = NotificationStore.getNotificationCount(prefs)

        setNotificationChannel()
        createNotification(notification, notificationCount)
        sendUpdateIntent()
    }

    private fun setNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = descriptionText
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(notification: Notification, notificationCount: Int) {
        if (
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(notification.title)
                .setContentText(notification.body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {
                notify(notificationCount, builder.build())
            }
        }
    }

    private fun sendUpdateIntent() {
        val intent = Intent()
        intent.action = INTENT_UPDATE
        sendBroadcast(intent)
    }
}