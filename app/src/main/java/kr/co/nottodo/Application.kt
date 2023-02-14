package kr.co.nottodo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import kr.co.nottodo.data.local.SharedPreference
import timber.log.Timber

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        setupTimber()
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        setupSharedPreferences()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    private fun setupSharedPreferences() {
        SharedPreference.init(this)
    }
}