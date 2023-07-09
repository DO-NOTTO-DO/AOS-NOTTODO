package kr.co.nottodo.presentation.modification.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.remote.api.ServicePool
import kr.co.nottodo.data.remote.api.ServicePool.modificationService
import kr.co.nottodo.data.remote.model.ResponseRecentMissionListDto
import kr.co.nottodo.data.remote.model.ResponseRecommendSituationListDto
import kr.co.nottodo.data.remote.model.modification.RequestModificationDto
import kr.co.nottodo.data.remote.model.modification.ResponseGetMissionDates
import kr.co.nottodo.data.remote.model.modification.ResponseModificationDto
import kr.co.nottodo.presentation.modification.view.ModificationActivity.Companion.NotTodoData
import kr.co.nottodo.presentation.recommendation.util.getErrorMessage
import kr.co.nottodo.view.calendar.monthly.util.achievementConvertStringToDate
import kr.co.nottodo.view.calendar.monthly.util.isToday
import kr.co.nottodo.view.calendar.monthly.util.isTomorrow

class ModificationViewModel : ViewModel() {

    private var originMission: String? = null
    private var originSituation: String? = null
    private var originalActionList: List<String>? = null
    private var originGoal: String? = null
    private var missionId: Long? = null

    fun setOriginalData(data: NotTodoData) {
        originMission = data.mission
        originSituation = data.situation
        originalActionList = data.actions ?: emptyList()
        originGoal = data.goal ?: ""

        mission.value = data.mission
        situation.value = data.situation
        actionList.value = data.actions ?: emptyList()
        actionCount.value = data.actions?.size ?: 0
        goal.value = data.goal ?: ""
        missionId = data.missionId
    }

    private val dates: MutableLiveData<List<String>> = MutableLiveData()
    val firstDate: LiveData<String> = dates.map { dates ->
        dates.last()
    }
    val dateDesc: LiveData<String> = firstDate.map { firstDate ->
        val date = firstDate.achievementConvertStringToDate()
        if (date?.isToday() == true) "오늘"
        else if (date?.isTomorrow() == true) {
            "내일"
        } else {
            ""
        }
    }
    val datesCountMinusOne: LiveData<String> = dates.map { dates ->
        if (dates.size == 1) {
            ""
        } else {
            "그 외 ${dates.size - 1}일"
        }
    }
    val mission: MutableLiveData<String> = MutableLiveData()
    private val isMissionChanged: LiveData<Boolean> = mission.map { newMission ->
        originMission != newMission
    }

    val situation: MutableLiveData<String> = MutableLiveData()
    private val isSituationChanged: LiveData<Boolean> = situation.map { newSituation ->
        originSituation != newSituation
    }

    val action: MutableLiveData<String> = MutableLiveData()
    val actionCount: MutableLiveData<Int> = MutableLiveData()
    val actionList: MutableLiveData<List<String>> = MutableLiveData()
    private val isActionListChanged: LiveData<Boolean> = actionList.map { newActionList ->
        originalActionList != newActionList
    }
    val goal: MutableLiveData<String> = MutableLiveData()
    private val isGoalChanged: LiveData<Boolean> = goal.map { newGoal ->
        originGoal != newGoal
    }

    val isAbleToModify: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        isAbleToModify.addSource(isMissionChanged) {
            isAbleToModify.value = _isAbleToModify()
        }
        isAbleToModify.addSource(isSituationChanged) {
            isAbleToModify.value = _isAbleToModify()
        }
        isAbleToModify.addSource(isActionListChanged) {
            isAbleToModify.value = _isAbleToModify()
        }
        isAbleToModify.addSource(isGoalChanged) {
            isAbleToModify.value = _isAbleToModify()
        }
    }

    private fun _isAbleToModify(): Boolean =
        isMissionChanged.value == true || isSituationChanged.value == true || isActionListChanged.value == true || isGoalChanged.value == true

    private val _modificationResponse: MutableLiveData<ResponseModificationDto.Modification> =
        MutableLiveData()
    val modificationResponse: LiveData<ResponseModificationDto.Modification>
        get() = _modificationResponse

    private val _errorResponse: MutableLiveData<String> = MutableLiveData()
    val errorResponse: LiveData<String>
        get() = _errorResponse

    fun putModifyMission() {
        viewModelScope.launch {
            kotlin.runCatching {
                modificationService.modifyMission(
                    missionId ?: throw NullPointerException("mission Id is null"),
                    RequestModificationDto(
                        title = requireNotNull(mission.value),
                        situation = requireNotNull(situation.value),
                        actions = actionList.value,
                        goal = goal.value
                    )
                )
            }.fold(onSuccess = { response -> _modificationResponse.value = response.data },
                onFailure = { errorResponse ->
                    _errorResponse.value = errorResponse.getErrorMessage()
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
                ServicePool.notTodoService.getRecommendSituationList()
            }.fold(onSuccess = { response ->
                _getRecommendSituationListSuccessResponse.value = response
            }, onFailure = { error ->
                _getRecommendSituationListErrorResponse.value = error.message
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
                ServicePool.notTodoService.getRecentMissionList()
            }.fold(onSuccess = { response ->
                _getRecentMissionListSuccessResponse.value = response
            }, onFailure = { error ->
                _getRecentMissionListErrorResponse.value = error.getErrorMessage()
            })
        }
    }

    private val _getMissionDatesSuccessResponse: MutableLiveData<ResponseGetMissionDates> =
        MutableLiveData()
    val getMissionDatesSuccessResponse: LiveData<ResponseGetMissionDates> =
        _getMissionDatesSuccessResponse

    private val _getMissionDatesErrorResponse: MutableLiveData<String> = MutableLiveData()
    val getMissionDatesErrorResponse: LiveData<String> = _getMissionDatesErrorResponse

    fun getMissionDates() {
        viewModelScope.launch {
            kotlin.runCatching {
                modificationService.getMissionDates(
                    missionId?.toInt() ?: throw NullPointerException("invalid mission id")
                )
            }.fold(onSuccess = { response -> _getMissionDatesSuccessResponse.value = response },
                onFailure = { errorResponse ->
                    _getMissionDatesErrorResponse.value = errorResponse.getErrorMessage()
                })
        }
    }

    fun setMissionDates() {
        dates.value = getMissionDatesSuccessResponse.value?.data ?: emptyList()
    }
}