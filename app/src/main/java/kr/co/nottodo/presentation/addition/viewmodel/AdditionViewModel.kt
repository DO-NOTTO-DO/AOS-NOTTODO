package kr.co.nottodo.presentation.addition.viewmodel

import androidx.lifecycle.*

class AdditionViewModel : ViewModel() {
    val mission: MutableLiveData<String> = MutableLiveData()
    val isMissionFilled: LiveData<Boolean> = Transformations.map(mission) {
        it.isNotBlank()
    }
    val situation: MutableLiveData<String> = MutableLiveData()
    val isSituationFilled: LiveData<Boolean> = Transformations.map(situation) {
        it.isNotBlank()
    }
    val action: MutableLiveData<String> = MutableLiveData()
    val isActionFilled: LiveData<Boolean> = Transformations.map(action) {
        it.isNotBlank()
    }
    val goal: MutableLiveData<String> = MutableLiveData()

    val isAbleToAdd: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        isAbleToAdd.addSource(isMissionFilled){
            isAbleToAdd.value = _isAbleToAdd()
        }
        isAbleToAdd.addSource(isSituationFilled){
            isAbleToAdd.value = _isAbleToAdd()
        }
        isAbleToAdd.addSource(isActionFilled){
            isAbleToAdd.value = _isAbleToAdd()
        }
    }
    private fun _isAbleToAdd(): Boolean {
        return (isMissionFilled.value == true
                && isMissionFilled.value == true
                && isSituationFilled.value == true
                && isActionFilled.value == true)
    }
}