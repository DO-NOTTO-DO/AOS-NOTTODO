package kr.co.nottodo.data.remote.api

import kr.co.nottodo.data.remote.model.ResponseRecentMissionListDto
import kr.co.nottodo.data.remote.model.ResponseRecommendSituationListDto
import retrofit2.http.GET

interface NotTodoService {
    @GET("recommend/situation")
    suspend fun getRecommendSituationList(): ResponseRecommendSituationListDto

    @GET("mission/recent")
    suspend fun getRecentMissionList(): ResponseRecentMissionListDto
}