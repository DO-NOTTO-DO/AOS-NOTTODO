import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import kr.co.nottodo.BuildConfig
import timber.log.Timber

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        setupTimber()
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
    }
    private fun setupTimber() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}