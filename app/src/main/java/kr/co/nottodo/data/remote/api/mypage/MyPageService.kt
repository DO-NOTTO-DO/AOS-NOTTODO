package kr.co.nottodo.data.remote.api.mypage

import kr.co.nottodo.data.resource.PublicPathString.PATH_WITHDRAWAL
import retrofit2.Response
import retrofit2.http.DELETE

interface MyPageService {
    @DELETE(PATH_WITHDRAWAL)
    suspend fun withdrawal(): Response<Unit>
}