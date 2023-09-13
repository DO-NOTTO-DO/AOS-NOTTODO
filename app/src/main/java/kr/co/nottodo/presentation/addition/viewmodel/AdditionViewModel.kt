package kr.co.nottodo.presentation.addition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.remote.api.ServicePool.additionService
import kr.co.nottodo.data.remote.api.ServicePool.notTodoService
import kr.co.nottodo.data.remote.model.ResponseRecentMissionListDto
import kr.co.nottodo.data.remote.model.ResponseRecommendSituationListDto
import kr.co.nottodo.data.remote.model.addition.RequestAdditionDto
import kr.co.nottodo.data.remote.model.addition.ResponseAdditionDto
import kr.co.nottodo.util.PublicString.NO_INTERNET_CONDITION_ERROR
import kr.co.nottodo.util.getErrorMessage
import kr.co.nottodo.util.isConnectException
import kr.co.nottodo.view.calendar.monthly.util.convertDateToString
import java.util.Date

class AdditionViewModel : ViewModel() {

    var isDateToggleVisible: Boolean = false
    var isMissionToggleVisible: Boolean = false
    var isSituationToggleVisible: Boolean = false
    var isActionToggleVisible: Boolean = false
    var isGoalToggleVisible: Boolean = false

    val date: MutableLiveData<String> = MutableLiveData(Date().convertDateToString())

    val mission: MutableLiveData<String> = MutableLiveData()
    private val isMissionFilled: LiveData<Boolean> = mission.map { mission ->
        mission.isNotBlank()
    }
    val situation: MutableLiveData<String> = MutableLiveData()
    private val isSituationFilled: LiveData<Boolean> = situation.map { situation ->
        situation.isNotBlank()
    }
    val action: MutableLiveData<String> = MutableLiveData()
    val goal: MutableLiveData<String> = MutableLiveData()

    val isAbleToAdd: MediatorLiveData<Boolean> = MediatorLiveData()

    val actionCount: MutableLiveData<Int> = MutableLiveData(0)

    init {
        isAbleToAdd.addSource(isMissionFilled) {
            isAbleToAdd.value = _isAbleToAdd()
        }
        isAbleToAdd.addSource(isSituationFilled) {
            isAbleToAdd.value = _isAbleToAdd()
        }
    }

    private fun _isAbleToAdd(): Boolean {
        return (isMissionFilled.value == true && isSituationFilled.value == true)
    }

    private val _postNottodoSuccessResponse: MutableLiveData<ResponseAdditionDto.Addition> =
        MutableLiveData()
    val postNottodoSuccessResponse: LiveData<ResponseAdditionDto.Addition>
        get() = _postNottodoSuccessResponse

    private val _postNottodoErrorMessage: MutableLiveData<String> = MutableLiveData()
    val postNottodoErrorMessage: LiveData<String>
        get() = _postNottodoErrorMessage

    fun postNottodo(requestAdditionDto: RequestAdditionDto) {
        viewModelScope.launch {
            kotlin.runCatching {
                additionService.postMission(requestAdditionDto)
            }.fold(onSuccess = { successResponse ->
                _postNottodoSuccessResponse.value = successResponse.data
            }, onFailure = { errorResponse ->
                _postNottodoErrorMessage.value =
                    if (errorResponse.isConnectException) NO_INTERNET_CONDITION_ERROR else errorResponse.getErrorMessage()
            })
        }
    }

    private val _getRecommendSituationListSuccessResponse: MutableLiveData<ResponseRecommendSituationListDto> =
        MutableLiveData()
    val getRecommendSituationListSuccessResponse: LiveData<ResponseRecommendSituationListDto> =
        _getRecommendSituationListSuccessResponse

    private val _getRecommendSituationListErrorResponse: MutableLiveData<String> = MutableLiveData()
    val getRecommendSituationListErrorResponse: LiveData<String> =
        _getRecommendSituationListErrorResponse

    fun getRecommendSituationList() {
        viewModelScope.launch {
            kotlin.runCatching {
                notTodoService.getRecommendSituationList()
            }.fold(onSuccess = { response ->
                _getRecommendSituationListSuccessResponse.value = response
            }, onFailure = { error ->
                _getRecommendSituationListErrorResponse.value =
                    if (error.isConnectException) NO_INTERNET_CONDITION_ERROR else error.message
            })
        }
    }

    private val _getRecentMissionListSuccessResponse: MutableLiveData<ResponseRecentMissionListDto> =
        MutableLiveData()
    val getRecentMissionListSuccessResponse: LiveData<ResponseRecentMissionListDto> =
        _getRecentMissionListSuccessResponse

    private val _getRecentMissionListErrorResponse: MutableLiveData<String> = MutableLiveData()
    val getRecentMissionListListErrorResponse: LiveData<String> = _getRecentMissionListErrorResponse

    fun getRecentMissionList() {
        viewModelScope.launch {
            kotlin.runCatching {
                notTodoService.getRecentMissionList()
            }.fold(onSuccess = { response ->
                _getRecentMissionListSuccessResponse.value = response
            }, onFailure = { error ->
                _getRecentMissionListErrorResponse.value =
                    if (error.isConnectException) NO_INTERNET_CONDITION_ERROR else error.message
            })
        }
    }
}