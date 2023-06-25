package kr.co.nottodo.data.remote.api

import kr.co.nottodo.data.remote.model.recommendation.mission.ResponseRecommendationMissionListDto
import retrofit2.http.GET

interface RecommendationMainListService {
    @GET("recommend/mission")
    suspend fun getRecommendationMainList(): ResponseRecommendationMissionListDto
}