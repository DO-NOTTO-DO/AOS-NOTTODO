package kr.co.nottodo.data.remote.api

import kr.co.nottodo.data.remote.model.RequestTokenDto
import kr.co.nottodo.data.remote.model.ResponseTokenDto
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenService {
    @POST
    suspend fun getToken(@Body request: RequestTokenDto): ResponseTokenDto
}