package kr.co.nottodo.data.remote.api.login

import kr.co.nottodo.data.remote.model.login.RequestTokenDto
import kr.co.nottodo.data.remote.model.login.ResponseTokenDto
import kr.co.nottodo.data.resource.PublicPathString.PATH_LOGIN
import kr.co.nottodo.data.resource.PublicPathString.PATH_SOCIAL
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface TokenService {
    @POST(PATH_LOGIN)
    suspend fun getToken(
        @Path(PATH_SOCIAL) social: String,
        @Body request: RequestTokenDto,
    ): ResponseTokenDto
}