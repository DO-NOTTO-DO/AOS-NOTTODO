package kr.co.nottodo.presentation.addition.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.remote.api.ServicePool
import kr.co.nottodo.data.remote.model.RequestAdditionDto
import kr.co.nottodo.data.remote.model.ResponseAdditionDto

class AdditionViewModel : ViewModel() {
    private val additionService by lazy { ServicePool.additionService }

    val mission: MutableLiveData<String> = MutableLiveData()
    private val isMissionFilled: LiveData<Boolean> = Transformations.map(mission) { mission ->
        mission.isNotBlank()
    }
    val situation: MutableLiveData<String> = MutableLiveData()
    private val isSituationFilled: LiveData<Boolean> = Transformations.map(situation) { situation ->
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
        return (isMissionFilled.value == true
                && isSituationFilled.value == true)
    }

    private val _additionResponse: MutableLiveData<ResponseAdditionDto.Addition> = MutableLiveData()
    val additionResponse: LiveData<ResponseAdditionDto.Addition>
        get() = _additionResponse

    private val _errorResponse: MutableLiveData<String> = MutableLiveData()
    val errorResponse: LiveData<String>
        get() = _errorResponse

    fun postAddition(requestAdditionDto: RequestAdditionDto) {
        viewModelScope.launch {
            kotlin.runCatching {
                additionService.postMission(requestAdditionDto)
            }.fold(onSuccess = { _additionResponse.value = it.data },
                onFailure = { _errorResponse.value = it.message })
        }
    }
}