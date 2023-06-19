package kr.co.nottodo.data.remote.api.mypage

import kr.co.nottodo.data.remote.model.mypage.WithdrawalDto
import retrofit2.http.DELETE

interface MyPageService {
    @DELETE("auth/withdrawal")
    suspend fun withdrawal(): WithdrawalDto
}