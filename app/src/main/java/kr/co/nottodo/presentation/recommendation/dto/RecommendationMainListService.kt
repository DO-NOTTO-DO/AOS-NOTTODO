package kr.co.nottodo.presentation.recommendation.dto

import RecommendationMainListDTO
import retrofit2.http.GET

interface RecommendationMainListService {
    @GET("/recommend/mission")
    suspend fun getRecommendationMainList(): RecommendationMainListDTO
}