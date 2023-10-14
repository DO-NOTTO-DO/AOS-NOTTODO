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
import kr.co.nottodo.util.PublicString.EMPTY_STRING
import kr.co.nottodo.util.PublicString.INPUT
import kr.co.nottodo.util.PublicString.MAX_COUNT_20
import kr.co.nottodo.util.PublicString.NO_INTERNET_CONDITION_ERROR
import kr.co.nottodo.util.addSourceList
import kr.co.nottodo.util.getErrorMessage
import kr.co.nottodo.util.isConnectException
import kr.co.nottodo.view.calendar.monthly.util.convertDateToString
import java.util.Date

class AdditionNewViewModel : ViewModel() {

    val isDateToggleVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isMissionToggleVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSituationToggleVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isActionToggleVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isGoalToggleVisible: MutableLiveData<Boolean> = MutableLiveData(false)

    val date: MutableLiveData<String> = MutableLiveData(Date().convertDateToString())

    val mission: MutableLiveData<String> = MutableLiveData("")
    val isMissionFilled: LiveData<Boolean> = mission.map { mission ->
        mission.isNotBlank()
    }
    val missionLengthCounter: LiveData<String> = mission.map { mission ->
        mission.length.toString() + MAX_COUNT_20
    }
    val situation: MutableLiveData<String> = MutableLiveData("")
    val situationLengthCounter: LiveData<String> = situation.map { situation ->
        situation.length.toString() + MAX_COUNT_20
    }
    val isSituationFilled: LiveData<Boolean> = situation.map { situation ->
        situation.isNotBlank()
    }

    val action: MutableLiveData<String> = MutableLiveData(EMPTY_STRING)
    val actionLengthCounter: LiveData<String> =
        action.map { action -> action.length.toString() + MAX_COUNT_20 }

    val actionList: MutableLiveData<List<String>> = MutableLiveData(emptyList())

    val firstAction: LiveData<String> = actionList.map { actionList ->
        actionList?.getOrNull(0) ?: EMPTY_STRING
    }
    val isFirstActionExist: LiveData<Boolean> =
        firstAction.map { firstAction -> firstAction.isNotBlank() }

    val secondAction: LiveData<String> = actionList.map { actionList ->
        actionList?.getOrNull(1) ?: EMPTY_STRING
    }
    val isSecondActionExist: LiveData<Boolean> =
        secondAction.map { secondAction -> secondAction.isNotBlank() }

    val thirdAction: LiveData<String> = actionList.map { actionList ->
        actionList?.getOrNull(2) ?: EMPTY_STRING
    }
    val isThirdActionExist: LiveData<Boolean> =
        thirdAction.map { thirdAction -> thirdAction.isNotBlank() }

    val actionCount: LiveData<Int> = actionList.map { actionList ->
        actionList?.size ?: 0
    }

    val actionListToString: LiveData<String> = actionList.map {
        updateActionList()
    }

    private fun updateActionList(): String = when (actionCount.value) {
        1 -> "${firstAction.value}"
        2 -> "${firstAction.value}\n${secondAction.value}"
        3 -> "${firstAction.value}\n${secondAction.value}\n${thirdAction.value}"
        else -> INPUT
    }

    val goal: MutableLiveData<String> = MutableLiveData("")
    val isGoalFilled: LiveData<Boolean> = goal.map { goal -> goal.isNotBlank() }
    val goalLengthCounter: LiveData<String> = goal.map { goal ->
        goal.length.toString() + MAX_COUNT_20
    }

    val isAbleToPost: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSourceList(isMissionFilled, isSituationFilled) {
            _isAbleToAdd()
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