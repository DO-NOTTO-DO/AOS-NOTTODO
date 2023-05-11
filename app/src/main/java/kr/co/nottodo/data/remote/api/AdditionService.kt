package kr.co.nottodo.data.remote.api

import kr.co.nottodo.data.remote.model.RequestAdditionDto
import kr.co.nottodo.data.remote.model.ResponseAdditionDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AdditionService {
    @POST("mission")
    suspend fun postMission(@Body request: RequestAdditionDto): ResponseAdditionDto
}