package kr.co.nottodo.data.remote.api

import kr.co.nottodo.data.resource.PublicPathString.PATH_GET_RECOMMEND_MISSION_LIST
import kr.co.nottodo.data.remote.model.recommendation.mission.ResponseRecommendMissionListDto
import retrofit2.http.GET

interface RecommendMissionListService {
    @GET(PATH_GET_RECOMMEND_MISSION_LIST)
    suspend fun getRecommendMissionList(): ResponseRecommendMissionListDto
}