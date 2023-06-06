package com.rulyox.personalnotifier.data

import android.content.SharedPreferences
import com.rulyox.personalnotifier.PREFS_KEY_COUNT
import com.rulyox.personalnotifier.PREFS_KEY_NOTI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object NotificationStore {
    fun getNotifications(prefs: SharedPreferences): ArrayList<Notification> {
        val jsonString = prefs.getString(PREFS_KEY_NOTI, null)

        return if (jsonString == null) ArrayList()
        else Json.decodeFromString(jsonString)
    }

    fun getNotificationCount(prefs: SharedPreferences): Int {
        return prefs.getInt(PREFS_KEY_COUNT, 0)
    }

    fun addNotification(prefs: SharedPreferences, notification: Notification) {
        with (prefs.edit()) {
            val jsonString = prefs.getString(PREFS_KEY_NOTI, null)

            val notifications =
                if (jsonString == null) ArrayList<Notification>()
                else Json.decodeFromString(jsonString)
            notifications.add(notification)

            putString(PREFS_KEY_NOTI, Json.encodeToString(notifications))

            val count = prefs.getInt(PREFS_KEY_COUNT, 0)
            putInt(PREFS_KEY_COUNT, count + 1)

            apply()
        }
    }

    fun clearNotification(prefs: SharedPreferences) {
        with (prefs.edit()) {
            putString(PREFS_KEY_NOTI, Json.encodeToString(ArrayList<Notification>()))
            apply()
        }
    }
}