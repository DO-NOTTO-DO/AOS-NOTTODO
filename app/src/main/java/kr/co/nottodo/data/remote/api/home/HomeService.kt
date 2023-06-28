package kr.co.nottodo.data.remote.api.home

import kr.co.nottodo.data.model.Home.*
import kr.co.nottodo.data.remote.model.FailureResponseDto
import kr.co.nottodo.data.remote.model.home.ResponHomeMissionDetail
import kr.co.nottodo.data.remote.model.home.ResponseHomeWeekly
import retrofit2.http.*

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

    @DELETE("mission/{missionId}")
    suspend fun deleteTodo(
        @Path("missionId") missionId: Long
    ): FailureResponseDto

    @POST("mission/{missionId}")
    suspend fun postDoAnotherDay(
        @Path("missionId") missionId: Long,
        @Body dates: RequestHomeDoAnotherDay
    ): ResponseHomeDoAnotherDayDtoDto
}