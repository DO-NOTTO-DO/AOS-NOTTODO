package kr.co.nottodo.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kr.co.nottodo.BuildConfig
import kr.co.nottodo.MainActivity.Companion.BLANK
import kr.co.nottodo.R
import kr.co.nottodo.presentation.login.view.LoginActivity

object SharedPreferences {
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        val masterKeyAlias = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

        preferences = if (BuildConfig.DEBUG) context.getSharedPreferences(
            context.getString(R.string.preference_file_name), Context.MODE_PRIVATE
        )
        else EncryptedSharedPreferences.create(
            context,
            context.getString(R.string.preference_file_name),
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun setString(key: String, value: String?) {
        preferences.edit { putString(key, value) }
    }

    fun getString(key: String): String? {
        return preferences.getString(key, BLANK)
    }

    fun setBoolean(key: String, value: Boolean) {
        preferences.edit { putBoolean(key, value) }
    }

    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    private fun clear() {
        preferences.edit { clear() }
    }

    fun clearForLogout() {
        clear()
        setBoolean(LoginActivity.DID_USER_WATCHED_ONBOARD, true)
    }
}