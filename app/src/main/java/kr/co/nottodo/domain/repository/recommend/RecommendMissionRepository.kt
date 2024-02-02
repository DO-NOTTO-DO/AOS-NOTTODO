package kr.co.nottodo.domain.repository.recommend

import kr.co.nottodo.data.remote.api.ServicePool
import kr.co.nottodo.data.remote.model.recommendation.mission.ResponseRecommendMissionListDto
import kr.co.nottodo.domain.entity.recommend.RecommendMissionDomainModel

class RecommendMissionRepository {
    suspend fun getRecommendMissionList(): RecommendMissionDomainModel =
        ServicePool.recommendMissionListService.getRecommendMissionList().asDomainModel()

    private fun ResponseRecommendMissionListDto.asDomainModel(): RecommendMissionDomainModel =
        RecommendMissionDomainModel(recommendMissionList = this.data.map {
            RecommendMissionDomainModel.Mission(
                id = it.id,
                missionTitle = it.title,
                missionDesc = it.description,
                situation = it.situation,
                imageUrl = it.image
            )
        })
}