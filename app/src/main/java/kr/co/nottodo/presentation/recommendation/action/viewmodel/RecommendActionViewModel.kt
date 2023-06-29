package kr.co.nottodo.presentation.recommendation.action.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.remote.api.ServicePool.recommendActionListService
import kr.co.nottodo.data.remote.model.recommendation.action.ResponseRecommendActionListDTO.Mission.Action

class RecommendActionViewModel : ViewModel() {
    private var missionId: Int? = null
    fun setMissionId(_missionId: Int) {
        missionId = _missionId
    }

    private val _recommendActionListSuccessResponse: MutableLiveData<List<Action>> =
        MutableLiveData()
    val recommendActionListSuccessResponse: LiveData<List<Action>>
        get() = _recommendActionListSuccessResponse

    private val _recommendActionListErrorResponse: MutableLiveData<String> = MutableLiveData()
    val recommendActionListErrorResponse: LiveData<String>
        get() = _recommendActionListErrorResponse

    fun getRecommendActionList() {
        viewModelScope.launch {
            kotlin.runCatching {
                recommendActionListService.getRecommendActionList(missionId ?: return@launch)
            }.fold(onSuccess = { response ->
                _recommendActionListSuccessResponse.value = response.data.recommendActions
            }, onFailure = { error -> _recommendActionListErrorResponse.value = error.message })
        }
    }
}