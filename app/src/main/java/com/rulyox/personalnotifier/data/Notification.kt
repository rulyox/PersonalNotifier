package com.rulyox.personalnotifier.data

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val timestamp: String,
    val title: String,
    val body: String
)