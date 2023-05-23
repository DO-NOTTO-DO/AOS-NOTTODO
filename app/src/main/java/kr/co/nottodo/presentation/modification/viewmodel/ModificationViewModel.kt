package kr.co.nottodo.presentation.modification.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kr.co.nottodo.data.remote.api.ServicePool.modificationService
import kr.co.nottodo.data.remote.model.FailureResponseDto
import kr.co.nottodo.data.remote.model.RequestModificationDto
import kr.co.nottodo.data.remote.model.ResponseModificationDto
import kr.co.nottodo.presentation.modification.view.ModificationActivity.Companion.NotTodoData
import retrofit2.HttpException
import timber.log.Timber

class ModificationViewModel : ViewModel() {

    private var originDate: String? = null
    private var originMission: String? = null
    private var originSituation: String? = null
    private var originalActionList: List<String>? = null
    private var originGoal: String? = null

    fun setOriginalData(data: NotTodoData) {
        originDate = data.date
        originMission = data.mission
        originSituation = data.situation
        originalActionList = data.actions
        originGoal = data.goal

        date.value = data.date
        mission.value = data.mission
        situation.value = data.situation
        actionList.value = data.actions ?: emptyList()
        actionCount.value = data.actions?.size ?: 0
        goal.value = data.goal ?: ""
    }

    val date: MutableLiveData<String> = MutableLiveData()
    private val isDateChanged: LiveData<Boolean> = Transformations.map(date) { newDate ->
        originDate != newDate
    }

    val mission: MutableLiveData<String> = MutableLiveData()
    private val isMissionChanged: LiveData<Boolean> = Transformations.map(mission) { newMission ->
        originMission != newMission
    }

    val situation: MutableLiveData<String> = MutableLiveData()
    private val isSituationChanged: LiveData<Boolean> =
        Transformations.map(situation) { newSituation ->
            originSituation != newSituation
        }

    val action: MutableLiveData<String> = MutableLiveData()
    val actionCount: MutableLiveData<Int> = MutableLiveData()
    val actionList: MutableLiveData<List<String>> = MutableLiveData()
    private val isActionListChanged: LiveData<Boolean> =
        Transformations.map(actionList) { newActionList ->
            originalActionList != newActionList
        }
    val goal: MutableLiveData<String> = MutableLiveData()
    private val isGoalChanged: LiveData<Boolean> = Transformations.map(goal) { newGoal ->
        originGoal != newGoal
    }

    val isAbleToModify: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        isAbleToModify.addSource(isDateChanged) {
            isAbleToModify.value = _isAbleToModify()
        }
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
        isDateChanged.value == true || isMissionChanged.value == true || isSituationChanged.value == true || isActionListChanged.value == true || isGoalChanged.value == true

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
                    1, RequestModificationDto(
                        title = requireNotNull(mission.value),
                        situation = requireNotNull(situation.value),
                        actions = actionList.value,
                        goal = goal.value
                    )
                )
            }.fold(onSuccess = { response -> _modificationResponse.value = response.data },
                onFailure = { errorResponse ->
                    _errorResponse.value = getErrorMessage(errorResponse)
                })
        }
    }

    private fun getErrorMessage(result: Throwable): String {
        when (result) {
            is HttpException -> {
                val data = Json.decodeFromString<FailureResponseDto>(
                    result.response()?.errorBody()?.string() ?: return "예기치 못한 에러 발생2"
                )
                return data.message
            }

            else -> {
                Timber.e(result.message.toString())
                return result.message.toString()
            }
        }
    }
}