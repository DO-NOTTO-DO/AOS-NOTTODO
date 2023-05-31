package kr.co.nottodo.presentation.recommendation.dto

import RecommendationActionListDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface RecommendationActionListService  {
    @GET("environment/{id}")
    suspend fun getActionList(@Path("id") id: Int): RecommendationActionListDTO
}