package kr.co.nottodo.presentation.recommendation.mission.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.remote.api.ServicePool.recommendMissionListService
import kr.co.nottodo.data.remote.model.recommendation.mission.ResponseRecommendMissionListDto.Mission

class RecommendMissionViewModel : ViewModel() {
    private val _recommendMissionListSuccessResponse: MutableLiveData<List<Mission>> =
        MutableLiveData()
    val recommendMissionListSuccessResponse: LiveData<List<Mission>>
        get() = _recommendMissionListSuccessResponse

    private val _recommendMissionListErrorResponse: MutableLiveData<String> = MutableLiveData()
    val recommendMissionListErrorResponse: LiveData<String>
        get() = _recommendMissionListErrorResponse

    fun getRecommendMissionList() {
        viewModelScope.launch {
            kotlin.runCatching {
                recommendMissionListService.getRecommendMissionList()
            }.fold(onSuccess = { response ->
                _recommendMissionListSuccessResponse.value = response.data
            }, onFailure = { error -> _recommendMissionListErrorResponse.value = error.message })
        }
    }
}
