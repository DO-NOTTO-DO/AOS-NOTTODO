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
import kr.co.nottodo.data.remote.model.modification.ResponseModificationDto
import kr.co.nottodo.presentation.modification.model.NotTodoData
import kr.co.nottodo.util.PublicString.INPUT
import kr.co.nottodo.util.PublicString.MAX_COUNT_20
import kr.co.nottodo.util.PublicString.MISSION_ID_IS_NULL
import kr.co.nottodo.util.PublicString.MISSION_IS_NULL
import kr.co.nottodo.util.PublicString.NO_INTERNET_CONDITION_ERROR
import kr.co.nottodo.util.PublicString.SITUATION_IS_NULL
import kr.co.nottodo.util.addSourceList
import kr.co.nottodo.util.getErrorMessage
import kr.co.nottodo.util.isConnectException
import kr.co.nottodo.view.calendar.monthly.util.achievementConvertStringToDate
import kr.co.nottodo.view.calendar.monthly.util.convertDateStringToInt
import kr.co.nottodo.view.calendar.monthly.util.isToday
import kr.co.nottodo.view.calendar.monthly.util.isTomorrow

class ModificationNewViewModel : ViewModel() {

    val isMissionToggleVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSituationToggleVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isActionToggleVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val isGoalToggleVisible: MutableLiveData<Boolean> = MutableLiveData(false)

    private var originMission: String? = null
    private var originSituation: String? = null
    private var originalActionList: List<String>? = null
    private var originGoal: String? = null
    private var missionId: Long? = null

    fun setOriginalData(data: NotTodoData) {
        originMission = data.mission
        originSituation = data.situation
        originalActionList = data.actions?.toMutableList() ?: emptyList()
        originGoal = data.goal ?: ""

        mission.value = data.mission
        situation.value = data.situation
        actionList.value = data.actions?.toMutableList() ?: mutableListOf()
        goal.value = data.goal ?: ""
        missionId = data.missionId
    }

    private val _getMissionDatesErrorResponse: MutableLiveData<String> = MutableLiveData()
    val getMissionDatesErrorResponse: LiveData<String> = _getMissionDatesErrorResponse

    fun getMissionDates() {
        viewModelScope.launch {
            kotlin.runCatching {
                modificationService.getMissionDates(requireNotNull(missionId?.toInt()) { MISSION_ID_IS_NULL })
            }.fold(onSuccess = { response -> dates.value = response.data },
                onFailure = { errorResponse ->
                    _getMissionDatesErrorResponse.value =
                        if (errorResponse.isConnectException) NO_INTERNET_CONDITION_ERROR else errorResponse.getErrorMessage()
                })
        }
    }

    val dates: MutableLiveData<List<String>> = MutableLiveData()
    fun getDateToIntList(): List<Int> =
        dates.value?.map { date -> date.convertDateStringToInt() } ?: emptyList()

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

    val mission: MutableLiveData<String> = MutableLiveData("")
    private val isMissionChanged: LiveData<Boolean> = mission.map { newMission ->
        originMission != newMission
    }
    val isMissionFilled: LiveData<Boolean> = mission.map { mission ->
        mission.isNotBlank()
    }
    val missionLengthCounter: LiveData<String> = mission.map { mission ->
        mission.length.toString() + MAX_COUNT_20
    }

    val situation: MutableLiveData<String> = MutableLiveData("")
    val isSituationFilled: LiveData<Boolean> = situation.map { situation ->
        situation.isNotBlank()
    }
    private val isSituationChanged: LiveData<Boolean> = situation.map { newSituation ->
        originSituation != newSituation
    }
    val situationLengthCounter: LiveData<String> = situation.map { situation ->
        situation.length.toString() + MAX_COUNT_20
    }

    val actionList: MutableLiveData<MutableList<String>> = MutableLiveData()
    private val isActionListChanged: LiveData<Boolean> = actionList.map { newActionList ->
        originalActionList != newActionList
    }

    val action: MutableLiveData<String> = MutableLiveData("")
    val actionLengthCounter: LiveData<String> =
        action.map { action -> action.length.toString() + MAX_COUNT_20 }

    val firstAction: LiveData<String> = actionList.map { actionList ->
        actionList.getOrNull(0) ?: ""
    }
    val isFirstActionExist: LiveData<Boolean> =
        firstAction.map { firstAction -> firstAction.isNotBlank() }

    val secondAction: LiveData<String> = actionList.map { actionList ->
        actionList.getOrNull(1) ?: ""
    }
    val isSecondActionExist: LiveData<Boolean> =
        secondAction.map { secondAction -> secondAction.isNotBlank() }

    val thirdAction: LiveData<String> = actionList.map { actionList ->
        actionList.getOrNull(2) ?: ""
    }
    val isThirdActionExist: LiveData<Boolean> =
        thirdAction.map { thirdAction -> thirdAction.isNotBlank() }

    val actionCount: LiveData<Int> = actionList.map { actionList ->
        actionList.size
    }

    val actionListToString: LiveData<String> = actionList.map {
        updateActionList()
    }

    private fun updateActionList(): String {
        if (isThirdActionExist.value == true) return "${firstAction.value}\n${secondAction.value}\n${thirdAction.value}"
        if (isSecondActionExist.value == true) return "${firstAction.value}\n${secondAction.value}"
        if (isFirstActionExist.value == true) return "${firstAction.value}"
        else return INPUT
    }

    val goal: MutableLiveData<String> = MutableLiveData("")
    val isGoalFilled: LiveData<Boolean> = goal.map { goal -> goal.isNotBlank() }
    private val isGoalChanged: LiveData<Boolean> = goal.map { newGoal ->
        originGoal != newGoal
    }
    val goalLengthCounter: LiveData<String> = goal.map { goal ->
        goal.length.toString() + MAX_COUNT_20
    }

    val isAbleToModify: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSourceList(
            isMissionChanged,
            isSituationChanged,
            isActionListChanged,
            isGoalChanged,
            isMissionFilled,
            isSituationFilled
        ) {
            checkIsAbleToModify()
        }
    }

    private fun checkIsAbleToModify(): Boolean =
        (isMissionChanged.value == true || isSituationChanged.value == true || isActionListChanged.value == true || isGoalChanged.value == true) && isMissionFilled.value == true && isSituationFilled.value == true

    private val _modifyNottodoSuccessResponse: MutableLiveData<ResponseModificationDto.Modification> =
        MutableLiveData()
    val modifyNottodoSuccessResponse: LiveData<ResponseModificationDto.Modification>
        get() = _modifyNottodoSuccessResponse

    private val _modifyNottodoErrorMessage: MutableLiveData<String> = MutableLiveData()
    val modifyNottodoErrorMessage: LiveData<String>
        get() = _modifyNottodoErrorMessage

    fun modifyNottodo() {
        viewModelScope.launch {
            kotlin.runCatching {
                modificationService.modifyMission(requireNotNull(missionId) { MISSION_ID_IS_NULL },
                    RequestModificationDto(title = requireNotNull(mission.value) { MISSION_IS_NULL },
                        situation = requireNotNull(situation.value) { SITUATION_IS_NULL },
                        actions = actionList.value,
                        goal = goal.value
                    )
                )
            }.fold(onSuccess = { response -> _modifyNottodoSuccessResponse.value = response.data },
                onFailure = { errorResponse ->
                    _modifyNottodoErrorMessage.value =
                        if (errorResponse.isConnectException) NO_INTERNET_CONDITION_ERROR else errorResponse.getErrorMessage()
                })
        }
    }

    private val _getRecommendSituationListSuccessResponse: MutableLiveData<ResponseRecommendSituationListDto> =
        MutableLiveData()
    val getRecommendSituationListSuccessResponse: LiveData<ResponseRecommendSituationListDto> =
        _getRecommendSituationListSuccessResponse

    private val _getRecommendSituationListErrorMessage: MutableLiveData<String> = MutableLiveData()
    val getRecommendSituationListErrorMessage: LiveData<String> =
        _getRecommendSituationListErrorMessage

    fun getRecommendSituationList() {
        viewModelScope.launch {
            kotlin.runCatching {
                ServicePool.notTodoService.getRecommendSituationList()
            }.fold(onSuccess = { response ->
                _getRecommendSituationListSuccessResponse.value = response
            }, onFailure = { error ->
                _getRecommendSituationListErrorMessage.value =
                    if (error.isConnectException) NO_INTERNET_CONDITION_ERROR else error.getErrorMessage()
            })
        }
    }

    private val _getRecentMissionListSuccessResponse: MutableLiveData<ResponseRecentMissionListDto> =
        MutableLiveData()
    val getRecentMissionListSuccessResponse: LiveData<ResponseRecentMissionListDto> =
        _getRecentMissionListSuccessResponse

    private val _getRecentMissionListErrorMessage: MutableLiveData<String> = MutableLiveData()
    val getRecentMissionListListErrorMessage: LiveData<String> = _getRecentMissionListErrorMessage

    fun getRecentMissionList() {
        viewModelScope.launch {
            kotlin.runCatching {
                ServicePool.notTodoService.getRecentMissionList()
            }.fold(onSuccess = { response ->
                _getRecentMissionListSuccessResponse.value = response
            }, onFailure = { error ->
                _getRecentMissionListErrorMessage.value =
                    if (error.isConnectException) NO_INTERNET_CONDITION_ERROR else error.getErrorMessage()
            })
        }
    }
}