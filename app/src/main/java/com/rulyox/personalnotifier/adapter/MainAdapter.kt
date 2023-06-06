package com.rulyox.personalnotifier.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rulyox.personalnotifier.R
import com.rulyox.personalnotifier.data.Notification

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    private var notifications: ArrayList<Notification> = ArrayList()

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timestampView: TextView
        val titleView: TextView
        val bodyView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            timestampView = view.findViewById(R.id.item_text_timestamp)
            titleView = view.findViewById(R.id.item_text_title)
            bodyView = view.findViewById(R.id.item_text_body)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_main, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.timestampView.text = notifications[position].timestamp
        viewHolder.titleView.text = notifications[position].title
        viewHolder.bodyView.text = notifications[position].body
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = notifications.size

    fun update(notifications: ArrayList<Notification>) {
        this.notifications = notifications
        notifyDataSetChanged()
    }
}