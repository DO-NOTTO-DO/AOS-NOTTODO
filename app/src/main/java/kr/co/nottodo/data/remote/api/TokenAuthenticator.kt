package kr.co.nottodo.data.remote.api

import kr.co.nottodo.BuildConfig
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        return response.request.newBuilder().header("Authorization", BuildConfig.ANDROID_TOKEN)
            .build()
    }
}