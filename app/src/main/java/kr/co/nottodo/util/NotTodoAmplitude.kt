package kr.co.nottodo.util

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import kr.co.nottodo.BuildConfig

object NotTodoAmplitude {
    private lateinit var amplitude: Amplitude

    fun initAmplitude(applicationContext: Context) {
        val apiKey =
            if (BuildConfig.DEBUG) BuildConfig.AMPLITUDE_API_KEY_DEV else BuildConfig.AMPLITUDE_API_KEY_PRODUCT
        amplitude = Amplitude(
            Configuration(
                apiKey = apiKey,
                context = applicationContext,
                trackingSessionEvents = false,
            ),
        )
    }

    fun setAmplitudeUserId(userId: String) {
        amplitude.setUserId(userId)
    }

    fun trackEvent(eventName: String) {
        amplitude.track(eventType = eventName)
    }

    fun <T> trackEventWithProperty(eventName: String, propertyName: String, propertyValue: T) {
        amplitude.track(
            eventType = eventName, eventProperties = mapOf(propertyName to propertyValue)
        )
    }

    fun trackEventWithProperty(eventName: String, propertyMap: Map<String, Any>) {
        amplitude.track(
            eventType = eventName, eventProperties = propertyMap
        )
    }

    fun trackEventWithPropertyList(
        eventName: String,
        properties: MutableMap<String, CharSequence>,
    ) {
        val eventProperties = mutableMapOf<String, Any>()
        properties.forEach { (propertyName, propertyValue) ->
            eventProperties[propertyName] = propertyValue.toString()
        }

        amplitude.track(
            eventType = eventName, eventProperties = eventProperties
        )
    }
}
