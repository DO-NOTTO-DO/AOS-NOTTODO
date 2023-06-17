package kr.co.nottodo.data.remote.api.mypage

import kr.co.nottodo.data.remote.model.mypage.WithdrawalDto
import retrofit2.http.GET

interface MyPageService {
    @GET("auth/withdrawal")
    suspend fun withdrawal(): WithdrawalDto
}