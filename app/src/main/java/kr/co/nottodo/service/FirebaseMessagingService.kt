package kr.co.nottodo.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.co.nottodo.MainActivity
import kr.co.nottodo.R
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.presentation.login.view.LoginFragment.Companion.DID_USER_CHOOSE_TO_BE_NOTIFIED
import kr.co.nottodo.util.NotTodoAmplitude.trackEventWithProperty

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        makePushAlarm(message)
    }

    private fun makePushAlarm(message: RemoteMessage) {
        val requestCode = 0
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        val builder = NotificationCompat.Builder(this, getString(R.string.channel_id))
            .setSmallIcon(R.drawable.ic_push_alarm)
            .setContentTitle(message.data[NOTIFICATION_CONTENT_TITLE_KEY])
            .setContentText(message.data[NOTIFICATION_CONTENT_TEXT_KEY])
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val channel = NotificationChannel(
            getString(R.string.channel_id),
            getString(R.string.channel_name),
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = getString(R.string.channel_desc)
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED && SharedPreferences.getBoolean(
                DID_USER_CHOOSE_TO_BE_NOTIFIED,
            )
        ) {
            notificationManager.notify(NOTIFICATION_ID, builder.build())
            trackEventWithProperty(
                getString(R.string.click_push_noti),
                getString(R.string.click_push_noti_key),
                getString(R.string.click_push_noti_value),
            )
        }
    }

    companion object {
        const val NOTIFICATION_ID = 0
        const val NOTIFICATION_CONTENT_TITLE_KEY = "title"
        const val NOTIFICATION_CONTENT_TEXT_KEY = "description"
    }
}
