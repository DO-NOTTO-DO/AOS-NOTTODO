package kr.co.nottodo.data.remote.api.home

import kr.co.nottodo.data.remote.model.achieve.ResponseAchieveCalenderDto
import retrofit2.http.*

interface AchieveService {

    @GET("mission/month/{month}")
    suspend fun getAchieveCalendery(
        @Path("month") month: String
    ): ResponseAchieveCalenderDto
}
