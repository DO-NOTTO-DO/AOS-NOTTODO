package kr.co.nottodo.data.remote.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kr.co.nottodo.BuildConfig
import kr.co.nottodo.data.remote.api.home.HomeService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object ApiFactory {

    private val json by lazy {
        Json {
            coerceInputValues = true
        }
    }

    private val client by lazy {
        OkHttpClient.Builder().addInterceptor(TokenInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }).authenticator(TokenAuthenticator())
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    private val clientForLogin by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }).build()
    }

    val retrofitForSocialLogin: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(clientForLogin)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    inline fun <reified T> create(): T = retrofit.create<T>(T::class.java)
    inline fun <reified T> createForToken(): T = retrofitForSocialLogin.create<T>(T::class.java)

}

object ServicePool {
    val tokenService = ApiFactory.createForToken<TokenService>()
    val additionService = ApiFactory.create<AdditionService>()
    val homeService = ApiFactory.create<HomeService>()
    val modificationService = ApiFactory.create<ModificationService>()
}