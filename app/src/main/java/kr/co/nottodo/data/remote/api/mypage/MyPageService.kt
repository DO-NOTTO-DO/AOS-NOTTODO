package kr.co.nottodo.data.remote.api.mypage

import retrofit2.Response
import retrofit2.http.DELETE

interface MyPageService {
    @DELETE("auth/withdrawal")
    suspend fun withdrawal(): Response<Unit>
}