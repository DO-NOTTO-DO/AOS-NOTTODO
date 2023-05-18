package kr.co.nottodo.data.remote.api

import kr.co.nottodo.data.remote.model.RequestModificationDto
import kr.co.nottodo.data.remote.model.ResponseModificationDto
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface ModificationService {
    @PUT("mission/{missionId}")
    suspend fun modifyMission(
        @Path("missionId") missionId: Int,
        @Body request: RequestModificationDto,
    ): ResponseModificationDto
}