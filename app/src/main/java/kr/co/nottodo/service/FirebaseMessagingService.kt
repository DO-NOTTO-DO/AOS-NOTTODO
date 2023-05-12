package kr.co.nottodo.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.co.nottodo.data.local.SharedPreferences

class NotTodoFCMService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        saveTokenInSharedPreferences(token)
    }

    private fun saveTokenInSharedPreferences(token: String) {
        SharedPreferences.setString(key = "fcm token", value = token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

    }
}