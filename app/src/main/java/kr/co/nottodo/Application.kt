package kr.co.nottodo

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import com.kakao.sdk.common.KakaoSdk
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.data.remote.api.ApiFactory
import kr.co.nottodo.listeners.OnTokenExpiredListener
import kr.co.nottodo.presentation.login.view.LoginActivity
import kr.co.nottodo.util.NotTodoDebugTree
import timber.log.Timber

class Application : Application(), OnTokenExpiredListener {
    override fun onCreate() {
        super.onCreate()
        setupTimber()
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        setupSharedPreferences()
        setupKakaoSdk()
        setHttpConnection()
    }

    private fun setHttpConnection() {
        (this as? OnTokenExpiredListener)?.let { ApiFactory.initRetrofit(it) }
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(NotTodoDebugTree())
        }
    }

    private fun setupSharedPreferences() {
        SharedPreferences.init(this)
    }

    private fun setupKakaoSdk() {
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    override fun onTokenExpired() {
        logout()
    }

    private fun logout() {
        clearForLogout()
        navigateToLogin()
    }

    private fun navigateToLogin() = startActivity(
        Intent(this, LoginActivity::class.java).setFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK,
        ),
    )

    private fun clearForLogout() {
        SharedPreferences.clearForLogout()
    }
}
