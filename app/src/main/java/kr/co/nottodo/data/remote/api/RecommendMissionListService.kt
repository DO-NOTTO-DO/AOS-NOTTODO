package kr.co.nottodo.data.remote.api

import kr.co.nottodo.data.remote.model.recommendation.mission.ResponseRecommendMissionListDto
import retrofit2.http.GET

interface RecommendMissionListService {
    @GET("recommend/mission")
    suspend fun getRecommendMissionList(): ResponseRecommendMissionListDto
}