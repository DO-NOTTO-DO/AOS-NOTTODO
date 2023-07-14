package kr.co.nottodo.data.remote.api

import kr.co.nottodo.data.resource.PublicPathString.PATH_GET_RECOMMEND_ACTION_LIST
import kr.co.nottodo.data.resource.PublicPathString.PATH_MISSION_ID
import kr.co.nottodo.data.remote.model.recommendation.action.ResponseRecommendActionListDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface RecommendActionListService {
    @GET(PATH_GET_RECOMMEND_ACTION_LIST)
    suspend fun getRecommendActionList(@Path(PATH_MISSION_ID) missionId: Int): ResponseRecommendActionListDTO
}
