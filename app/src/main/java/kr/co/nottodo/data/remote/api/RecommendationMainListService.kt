package kr.co.nottodo.data.remote.api

import RecommendationMainListDTO
import retrofit2.http.GET

interface RecommendationMainListService {
    @GET("/recommend/mission")
    suspend fun getRecommendationMainList(): RecommendationMainListDTO
}