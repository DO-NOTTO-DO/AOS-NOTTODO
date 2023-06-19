package kr.co.nottodo.data.remote.api.addition

import kr.co.nottodo.data.remote.model.addition.RequestAdditionDto
import kr.co.nottodo.data.remote.model.addition.ResponseAdditionDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AdditionService {
    @POST("mission")
    suspend fun postMission(@Body request: RequestAdditionDto): ResponseAdditionDto
}