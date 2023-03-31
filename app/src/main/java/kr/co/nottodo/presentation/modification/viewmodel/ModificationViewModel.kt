package kr.co.nottodo.presentation.modification.viewmodel

import androidx.lifecycle.*
import kr.co.nottodo.presentation.modification.view.ModificationActivity.Companion.NotTodoData

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
        originalActionList = data.action
        originGoal = data.goal

        date.value = data.date
        mission.value = data.mission
        situation.value = data.situation
        actionList.value = data.action
        actionCount.value = data.action.size
        goal.value = data.goal
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

    private fun _isAbleToModify(): Boolean {
        return (isDateChanged.value == true || isMissionChanged.value == true || isSituationChanged.value == true || isActionListChanged.value == true || isGoalChanged.value == true)
    }
}