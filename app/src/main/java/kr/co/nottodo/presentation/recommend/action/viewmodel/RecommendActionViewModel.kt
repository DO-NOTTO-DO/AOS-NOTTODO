package kr.co.nottodo.presentation.recommend.action.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.remote.api.ServicePool.recommendActionListService
import kr.co.nottodo.data.remote.model.recommendation.action.ResponseRecommendActionListDTO.Mission.Action
import kr.co.nottodo.util.PublicString.NO_INTERNET_CONDITION_ERROR
import kr.co.nottodo.util.isConnectException

class RecommendActionViewModel : ViewModel() {
    private var missionId: Int? = null
    fun setMissionId(missionId: Int) {
        this.missionId = missionId
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
            }, onFailure = { error ->
                _recommendActionListErrorResponse.value =
                    if (error.isConnectException) NO_INTERNET_CONDITION_ERROR else error.message
            })
        }
    }

    private val selectedActionsCount = MutableLiveData(0)
    val isActionSelected = selectedActionsCount.map { it != 0 }
    val isSelectedActionsCountThree: () -> Boolean = {
        selectedActionsCount.value == 3
    }

    val plusSelectedActionsCount = {
        selectedActionsCount.value = (selectedActionsCount.value ?: 0) + 1
    }
    val minusSelectedActionsCount =
        { selectedActionsCount.value = selectedActionsCount.value?.minus(1) ?: 0 }

    fun resetActionsCount() {
        selectedActionsCount.value = 0
    }
}