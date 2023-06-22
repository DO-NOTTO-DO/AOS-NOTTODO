package kr.co.nottodo.data.remote.api.modification

import kr.co.nottodo.data.remote.model.modification.RequestModificationDto
import kr.co.nottodo.data.remote.model.modification.ResponseModificationDto
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface ModificationService {
    @PUT("mission/{missionId}")
    suspend fun modifyMission(
        @Path("missionId") missionId: Long,
        @Body request: RequestModificationDto,
    ): ResponseModificationDto
}