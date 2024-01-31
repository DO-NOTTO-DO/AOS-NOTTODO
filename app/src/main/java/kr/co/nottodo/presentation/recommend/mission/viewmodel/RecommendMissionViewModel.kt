package kr.co.nottodo.presentation.recommend.mission.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.domain.entity.recommend.RecommendMissionDomainModel
import kr.co.nottodo.domain.repository.recommend.RecommendMissionRepository
import kr.co.nottodo.util.PublicString.NO_INTERNET_CONDITION_ERROR
import kr.co.nottodo.util.isConnectException

class RecommendMissionViewModel : ViewModel() {

    private val recommendMissionRepository: RecommendMissionRepository =
        RecommendMissionRepository()

    private val _recommendMissionListSuccessResponse: MutableLiveData<RecommendMissionDomainModel> =
        MutableLiveData()
    val recommendMissionListSuccessResponse: LiveData<RecommendMissionDomainModel>
        get() = _recommendMissionListSuccessResponse

    private val _recommendMissionListErrorResponse: MutableLiveData<String> = MutableLiveData()
    val recommendMissionListErrorResponse: LiveData<String>
        get() = _recommendMissionListErrorResponse

    fun getRecommendMissionList() {
        viewModelScope.launch {
            kotlin.runCatching {
                recommendMissionRepository.getRecommendMissionList()
            }.fold(onSuccess = { recommendMissionDomainModel ->
                _recommendMissionListSuccessResponse.value = recommendMissionDomainModel
            }, onFailure = { error ->
                _recommendMissionListErrorResponse.value =
                    if (error.isConnectException) NO_INTERNET_CONDITION_ERROR else error.message
            })
        }
    }
}
