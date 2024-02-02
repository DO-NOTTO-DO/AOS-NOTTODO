package kr.co.nottodo.domain.entity.recommend

data class RecommendMissionDomainModel(
    val recommendMissionList: List<Mission>,
) {
    data class Mission(
        val id: Int,
        val missionTitle: String,
        val missionDesc: String,
        val situation: String,
        val imageUrl: String,
    )
}
