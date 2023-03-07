package kr.co.nottodo.presentation.modification.viewmodel

import androidx.lifecycle.*

class ModificationViewModel : ViewModel() {
    val mission: MutableLiveData<String> = MutableLiveData()
    private val isMissionFilled: LiveData<Boolean> = Transformations.map(mission) {
        it.isNotBlank()
    }
    val situation: MutableLiveData<String> = MutableLiveData()
    private val isSituationFilled: LiveData<Boolean> = Transformations.map(situation) {
        it.isNotBlank()
    }
    val action: MutableLiveData<String> = MutableLiveData()
    val goal: MutableLiveData<String> = MutableLiveData()

    val isAbleToAdd: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        isAbleToAdd.addSource(isMissionFilled) {
            isAbleToAdd.value = _isAbleToAdd()
        }
        isAbleToAdd.addSource(isSituationFilled) {
            isAbleToAdd.value = _isAbleToAdd()
        }
    }

    private fun _isAbleToAdd(): Boolean {
        return (isMissionFilled.value == true
                && isSituationFilled.value == true)
    }
}