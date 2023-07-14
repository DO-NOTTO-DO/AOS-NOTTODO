package kr.co.nottodo.data.remote.api.addition

import kr.co.nottodo.data.resource.PublicPathString.PATH_MISSION
import kr.co.nottodo.data.remote.model.addition.RequestAdditionDto
import kr.co.nottodo.data.remote.model.addition.ResponseAdditionDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AdditionService {
    @POST(PATH_MISSION)
    suspend fun postMission(@Body request: RequestAdditionDto): ResponseAdditionDto
}