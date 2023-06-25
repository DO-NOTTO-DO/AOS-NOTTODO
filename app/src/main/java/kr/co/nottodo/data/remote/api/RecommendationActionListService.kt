package kr.co.nottodo.data.remote.api

import kr.co.nottodo.data.remote.model.recommendation.action.ResponseRecommendationActionListDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface RecommendationActionListService {
    @GET("recommend/{missionId}/action")
    suspend fun getActionCategoryList(@Path("missionId") missionId: Int): ResponseRecommendationActionListDTO
}
