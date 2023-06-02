package kr.co.nottodo.data.remote.api

import kr.co.nottodo.data.remote.model.RequestTokenDto
import kr.co.nottodo.data.remote.model.ResponseTokenDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface TokenService {
    @POST("auth/login/{social}")
    suspend fun getToken(
        @Path("social") social: String,
        @Body request: RequestTokenDto,
    ): ResponseTokenDto
}