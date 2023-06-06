package com.rulyox.personalnotifier.activity

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.firebase.messaging.FirebaseMessaging
import com.rulyox.personalnotifier.INTENT_UPDATE
import com.rulyox.personalnotifier.PREFS_NAME
import com.rulyox.personalnotifier.R
import com.rulyox.personalnotifier.adapter.MainAdapter
import com.rulyox.personalnotifier.data.Notification
import com.rulyox.personalnotifier.data.NotificationStore

class MainActivity : AppCompatActivity() {
    private val adapter = MainAdapter()
    private var token: String = ""
    private var notifications: ArrayList<Notification> = ArrayList()

    inner class UpdateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.action == INTENT_UPDATE) loadNotifications()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRecycler(adapter)
        setReceiver()
        loadToken()
        loadNotifications()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_token -> {
                showDialog(this)
                true
            }
            R.id.main_menu_delete -> {
                val prefs: SharedPreferences = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                NotificationStore.clearNotification(prefs)
                notifications = NotificationStore.getNotifications(prefs)
                adapter.update(notifications)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setRecycler(adapter: MainAdapter) {
        val recyclerView: RecyclerView = findViewById(R.id.main_recycler)
        recyclerView.adapter = adapter
        val decoration = DividerItemDecoration(this, VERTICAL)
        recyclerView.addItemDecoration(decoration)
    }

    private fun setReceiver() {
        val receiver = UpdateReceiver()
        val filter = IntentFilter()
        filter.addAction(INTENT_UPDATE)
        registerReceiver(receiver, filter)
    }

    private fun loadToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            token = it
        }
    }

    private fun loadNotifications() {
        val prefs: SharedPreferences = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        notifications = NotificationStore.getNotifications(prefs)
        adapter.update(notifications)
    }

    private fun showDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.text_firebase_token))
            .setMessage(token)
            .setPositiveButton(getString(R.string.button_copy)
            ) { _, _ ->
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText(getString(R.string.text_firebase_token), token)
                clipboard.setPrimaryClip(clip)
            }
            .setNegativeButton(getString(R.string.button_close)
            ) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}