package kr.co.nottodo.data.resource

object PublicPathString {
    const val PATH_MISSION = "mission"
    const val PATH_LOGIN = "auth/login/{social}"
    const val PATH_SOCIAL = "social"
    const val PATH_MODIFY = "mission/{missionId}"
    const val PATH_MISSION_ID = "missionId"
    const val PATH_GET_MISSION_DATES = "mission/{missionId}/dates"
    const val PATH_WITHDRAWAL = "auth/withdrawal"
    const val PATH_GET_RECOMMEND_SITUATION_LIST = "recommend/situation"
    const val PATH_GET_RECENT_MISSION_LIST = "mission/recent"
    const val PATH_GET_RECOMMEND_ACTION_LIST = "recommend/{missionId}/action"
    const val PATH_GET_RECOMMEND_MISSION_LIST = "recommend/mission"
    const val AUTHORIZATION = "Authorization"
}