package kr.co.nottodo.data.remote.api.home

import kr.co.nottodo.data.model.Home.HomeDailyResponse
import kr.co.nottodo.data.model.Home.RequestHomeMissionCheck
import kr.co.nottodo.data.model.Home.ResponseHomeMissionCheckDto
import kr.co.nottodo.data.remote.model.home.ResponHomeMissionDetail
import kr.co.nottodo.data.remote.model.home.ResponseHomeWeekly
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface HomeService {
    @GET("mission/daily/{date}")
    suspend fun getHomeDaily(
        @Path("date") date: String
    ): HomeDailyResponse

    @PATCH("mission/{missionId}/check")
    suspend fun patchTodo(
        @Path("missionId") missionId: Long,
        @Body body: RequestHomeMissionCheck
    ): ResponseHomeMissionCheckDto

    @GET("mission/{missionId}")
    suspend fun getHomeBottomDetail(
        @Path("missionId") missionId: Long
    ): ResponHomeMissionDetail

    @GET("mission/week/{startDate}")
    suspend fun getHomeWeekly(
        @Path("startDate") startDate: String
    ): ResponseHomeWeekly

}