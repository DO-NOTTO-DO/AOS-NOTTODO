package kr.co.nottodo.data.remote.api

import kr.co.nottodo.data.remote.model.recommendation.action.ResponseRecommendActionListDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface RecommendActionListService {
    @GET("recommend/{missionId}/action")
    suspend fun getRecommendActionList(@Path("missionId") missionId: Int): ResponseRecommendActionListDTO
}
