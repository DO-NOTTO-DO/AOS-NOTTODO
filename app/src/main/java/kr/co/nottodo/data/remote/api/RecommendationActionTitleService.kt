package kr.co.nottodo.data.remote.api

import RecommendationActionListDTO
import retrofit2.http.GET

interface RecommendationActionTitleService {
    @GET("environment/category")
    suspend fun getActionTitleList(): RecommendationActionListDTO
}