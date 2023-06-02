package kr.co.nottodo.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
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
        Timber.tag("fcm").e("메시지가 오긴 왔습니다. ${message.data} ${message.notification}")

        makePushAlarm(message)

    }

    private fun makePushAlarm(message: RemoteMessage) {

        val builder = NotificationCompat.Builder(this, getString(R.string.channel_id))
            .setSmallIcon(R.drawable.ic_push_alarm)
            .setContentTitle(message.notification?.title ?: "")
            .setContentText(message.notification?.body ?: "")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val channel =
            NotificationChannel(
                getString(R.string.channel_id),
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.channel_desc)
            }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(0, builder.build())
            Timber.tag("fcm").e("알람이 만들어졌네요.")
        }
    }
}