package com.padabajka.dating.feature.push.notification.model

enum class NotificationChannel(val raw: String, val description: String) {
    Message("message", "New Message"),
    Match("match", "New Match"),
    Likes("likes", "New Like")
}
