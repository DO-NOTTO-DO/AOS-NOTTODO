package kr.co.nottodo.data.remote.api

import kr.co.nottodo.data.local.SharedPreferences.getString
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.USER_TOKEN
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val tokenAddedRequest = originalRequest.newBuilder()
            .header(
                "Authorization",
                getString(USER_TOKEN) ?: ""
            )
            .build()
        return chain.proceed(tokenAddedRequest)
    }
}