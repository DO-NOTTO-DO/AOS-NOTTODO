package kr.co.nottodo.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.co.nottodo.R
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.FCM_TOKEN
import timber.log.Timber


class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        saveTokenInSharedPreferences(token)
    }

    private fun saveTokenInSharedPreferences(token: String) {
        SharedPreferences.setString(key = FCM_TOKEN, value = token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.takeIf { it.data.isNotEmpty() }
        Timber.tag("fcm").e("메시지가 오긴 왔습니다. ${message.data} ${message.notification}")

        val notificationManager = NotificationManagerCompat.from(
            applicationContext
        )
        val builder =
            NotificationCompat.Builder(applicationContext, getString(R.string.channel_name))
        if (notificationManager.getNotificationChannel(getString(R.string.channel_name)) == null) {
            val channel = NotificationChannel(
                getString(R.string.channel_id),
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = getString(R.string.channel_desc) }
            notificationManager.createNotificationChannel(channel)
        }

        val title: String = message.notification?.title ?: ""
        val body: String = message.notification?.body ?: ""

        builder.setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_launcher_background)

        val notification: Notification = builder.build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(1, notification)
        }
    }
}