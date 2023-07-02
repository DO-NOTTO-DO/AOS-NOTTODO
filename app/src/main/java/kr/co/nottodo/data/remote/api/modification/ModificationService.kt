package kr.co.nottodo.data.remote.api.modification

import kr.co.nottodo.data.remote.model.modification.RequestModificationDto
import kr.co.nottodo.data.remote.model.modification.ResponseGetMissionDates
import kr.co.nottodo.data.remote.model.modification.ResponseModificationDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ModificationService {
    @PUT("mission/{missionId}")
    suspend fun modifyMission(
        @Path("missionId") missionId: Long,
        @Body request: RequestModificationDto,
    ): ResponseModificationDto

    @GET("mission/{missionId}/dates")
    suspend fun getMissionDates(
        @Path("missionId") missionId: Int,
    ): ResponseGetMissionDates
}