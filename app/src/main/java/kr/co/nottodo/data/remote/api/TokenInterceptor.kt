package kr.co.nottodo.data.remote.api

import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.USER_TOKEN
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
        val userToken = SharedPreferences.getString(USER_TOKEN)
        if (userToken != null) {
            newRequest.header(
                "Authorization", userToken
            )
        }
        return chain.proceed(newRequest.build())
    }
}