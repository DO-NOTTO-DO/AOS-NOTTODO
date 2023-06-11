package kr.co.nottodo.data.remote.api

import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.listeners.OnTokenExpiredListener
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.USER_TOKEN
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val onTokenExpiredListener: OnTokenExpiredListener) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val userToken = SharedPreferences.getString(USER_TOKEN)

        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
        if (userToken != null) {
            newRequest.header(
                "Authorization", userToken
            )
        }
        val response = chain.proceed(newRequest.build())
        if (response.code == 401)
            onTokenExpiredListener.onTokenExpired()

        return response
    }
}