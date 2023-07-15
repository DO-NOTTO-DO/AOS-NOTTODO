package kr.co.nottodo.data.remote.api

import kr.co.nottodo.data.resource.PublicPathString.PATH_GET_RECENT_MISSION_LIST
import kr.co.nottodo.data.resource.PublicPathString.PATH_GET_RECOMMEND_SITUATION_LIST
import kr.co.nottodo.data.remote.model.ResponseRecentMissionListDto
import kr.co.nottodo.data.remote.model.ResponseRecommendSituationListDto
import retrofit2.http.GET

interface NotTodoService {
    @GET(PATH_GET_RECOMMEND_SITUATION_LIST)
    suspend fun getRecommendSituationList(): ResponseRecommendSituationListDto

    @GET(PATH_GET_RECENT_MISSION_LIST)
    suspend fun getRecentMissionList(): ResponseRecentMissionListDto
}