package kr.co.nottodo.data.remote.api.modification

import kr.co.nottodo.data.resource.PublicPathString.PATH_GET_MISSION_DATES
import kr.co.nottodo.data.resource.PublicPathString.PATH_MISSION_ID
import kr.co.nottodo.data.resource.PublicPathString.PATH_MODIFY
import kr.co.nottodo.data.remote.model.modification.RequestModificationDto
import kr.co.nottodo.data.remote.model.modification.ResponseGetMissionDates
import kr.co.nottodo.data.remote.model.modification.ResponseModificationDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ModificationService {
    @PUT(PATH_MODIFY)
    suspend fun modifyMission(
        @Path(PATH_MISSION_ID) missionId: Long,
        @Body request: RequestModificationDto,
    ): ResponseModificationDto

    @GET(PATH_GET_MISSION_DATES)
    suspend fun getMissionDates(
        @Path(PATH_MISSION_ID) missionId: Int,
    ): ResponseGetMissionDates
}