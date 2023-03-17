package kr.co.nottodo.presentation.modification.viewmodel

import androidx.lifecycle.*
import kr.co.nottodo.presentation.modification.view.ModificationActivity.Companion.NotTodoData

class ModificationViewModel : ViewModel() {

    private var originDate: String? = null
    private var originMission: String? = null
    private var originSituation: String? = null
    private var originAction: String? = null
    private var originGoal: String? = null

    fun setOriginalData(data: NotTodoData) {
        originDate = data.date
        originMission = data.mission
        originSituation = data.situation
        originAction = data.action[0]
        originGoal = data.goal

        date.value = data.date
        mission.value = data.mission
        situation.value = data.situation
        action.value = data.action[0]
        goal.value = data.goal
    }

    val date: MutableLiveData<String> = MutableLiveData()
    private val isDateChanged: LiveData<Boolean> = Transformations.map(date) {
        originDate != it
    }

    val mission: MutableLiveData<String> = MutableLiveData()
    private val isMissionChanged: LiveData<Boolean> = Transformations.map(mission) {
        originMission != it
    }

    val situation: MutableLiveData<String> = MutableLiveData()
    private val isSituationChanged: LiveData<Boolean> = Transformations.map(situation) {
        originSituation != it
    }

    val action: MutableLiveData<String> = MutableLiveData()
    private val isActionChanged: LiveData<Boolean> = Transformations.map(action) {
        originAction != it
    }

    val goal: MutableLiveData<String> = MutableLiveData()
    private val isGoalChanged: LiveData<Boolean> = Transformations.map(goal) {
        originGoal != it
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
        isAbleToModify.addSource(isActionChanged) {
            isAbleToModify.value = _isAbleToModify()
        }
        isAbleToModify.addSource(isGoalChanged) {
            isAbleToModify.value = _isAbleToModify()
        }
    }

    private fun _isAbleToModify(): Boolean {
        return (isDateChanged.value == true
                || isMissionChanged.value == true
                || isSituationChanged.value == true
                || isActionChanged.value == true
                || isGoalChanged.value == true)
    }
}