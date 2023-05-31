package kr.co.nottodo.presentation.recommendation.dto

import RecommendationActionListDTO
import retrofit2.http.GET

interface RecommendationActionTitleService {
    @GET("environment/category")
    suspend fun getActionTitleList(): RecommendationActionListDTO
}