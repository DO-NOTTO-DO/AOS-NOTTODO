package kr.co.nottodo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.FCM_TOKEN
import timber.log.Timber

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        setupTimber()
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        setupSharedPreferences()
        setupKakaoSdk()
        setFCMToken()
    }

    private fun setFCMToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            SharedPreferences.setString(FCM_TOKEN, token)
        }
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    private fun setupSharedPreferences() {
        SharedPreferences.init(this)
    }

    private fun setupKakaoSdk() {
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}