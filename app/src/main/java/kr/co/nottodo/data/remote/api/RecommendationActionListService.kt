package kr.co.nottodo.data.remote.api

import RecommendationActionListDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface RecommendationActionListService  {
    @GET("environment/{id}")
    suspend fun getActionCategoryList(@Path("id") id: Int): RecommendationActionListDTO
}