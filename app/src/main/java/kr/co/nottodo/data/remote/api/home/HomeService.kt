package kr.co.nottodo.data.remote.api.home

import kr.co.nottodo.data.model.Home.HomeDailyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeService {
    @GET("mission/daily/{date}")
    suspend fun getHomeDaily(
        @Path("date") date: String
    ): HomeDailyResponse

}