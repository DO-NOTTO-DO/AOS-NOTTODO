package kr.co.nottodo.data.remote.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kr.co.nottodo.BuildConfig
import kr.co.nottodo.data.remote.api.addition.AdditionService
import kr.co.nottodo.data.remote.api.home.AchieveService
import kr.co.nottodo.data.remote.api.home.HomeService
import kr.co.nottodo.data.remote.api.login.TokenService
import kr.co.nottodo.data.remote.api.modification.ModificationService
import kr.co.nottodo.data.remote.api.mypage.MyPageService
import kr.co.nottodo.data.remote.interceptor.TokenInterceptor
import kr.co.nottodo.listeners.OnTokenExpiredListener
import kr.co.nottodo.presentation.recommendation.dto.RecommendationActionListService
import kr.co.nottodo.presentation.recommendation.dto.RecommendationActionTitleService
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

    private lateinit var client: OkHttpClient
    lateinit var retrofit: Retrofit

    fun initRetrofit(onTokenExpiredListener: OnTokenExpiredListener) {
        client = OkHttpClient.Builder().addInterceptor(TokenInterceptor(onTokenExpiredListener))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }).build()

        retrofit = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType())).build()
    }

    inline fun <reified T> create(): T = retrofit.create(T::class.java)
}

object ServicePool {
    val tokenService = ApiFactory.create<TokenService>()
    val additionService = ApiFactory.create<AdditionService>()
    val homeService = ApiFactory.create<HomeService>()
    val achieveService = ApiFactory.create<AchieveService>()
    val modificationService = ApiFactory.create<ModificationService>()
    val recommendationActionListService = ApiFactory.create<RecommendationActionListService>()
    val recommendationActionTitleService = ApiFactory.create<RecommendationActionTitleService>()
    val myPageService = ApiFactory.create<MyPageService>()
}
