package kr.co.nottodo.util

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import kr.co.nottodo.BuildConfig

object Amplitude {
    private lateinit var amplitude: Amplitude

    fun initAmplitude(applicationContext: Context) {
        amplitude = Amplitude(
            Configuration(
                apiKey = BuildConfig.AMPLITUDE_API_KEY,
                context = applicationContext,
                trackingSessionEvents = false
            )
        )
    }

    fun trackEvent(eventName: String) {
        amplitude.track(eventType = eventName)
    }

    fun <T> trackEventWithProperty(eventName: String, propertyName: String, propertyValue: T) {
        amplitude.track(
            eventType = eventName, eventProperties = mapOf(propertyName to propertyValue)
        )
    }
}