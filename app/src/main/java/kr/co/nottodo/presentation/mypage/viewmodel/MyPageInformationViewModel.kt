package kr.co.nottodo.presentation.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.remote.api.ServicePool.myPageService

class MyPageInformationViewModel : ViewModel() {

    private val _isWithdrawalSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isWithdrawalSuccess: LiveData<Boolean> = _isWithdrawalSuccess

    private val _withdrawalErrorResponse: MutableLiveData<String> = MutableLiveData()
    val withdrawalErrorResponse: LiveData<String> = _withdrawalErrorResponse

    fun withdrawal() {
        viewModelScope.launch {
            kotlin.runCatching {
                myPageService.withdrawal()
            }.fold(onSuccess = {
                val isWithdrawalSuccess = isWithdrawalSuccess.value
                _isWithdrawalSuccess.value = isWithdrawalSuccess ?: false
            }, onFailure = { error -> _withdrawalErrorResponse.value = error.message })
        }
    }

    private val _isNotificationPermissionValid: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNotificationPermissionValid: LiveData<Boolean> = _isNotificationPermissionValid
    fun setIsNotificationPermissionValid(isNotificationPermissionValid: Boolean) {
        _isNotificationPermissionValid.value = isNotificationPermissionValid
    }
}