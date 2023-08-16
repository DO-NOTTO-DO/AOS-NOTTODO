package kr.co.nottodo.presentation.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.remote.api.ServicePool.myPageService
import kr.co.nottodo.util.PublicString.NO_INTERNET_CONDITION_ERROR
import kr.co.nottodo.util.isConnectException

class MyPageInformationViewModel : ViewModel() {

    private val _withdrawalSuccessResponse: MutableLiveData<Boolean> = MutableLiveData()
    val withdrawalSuccessResponse: LiveData<Boolean> = _withdrawalSuccessResponse

    private val _withdrawalErrorResponse: MutableLiveData<String> = MutableLiveData()
    val withdrawalErrorResponse: LiveData<String> = _withdrawalErrorResponse

    fun withdrawal() {
        viewModelScope.launch {
            kotlin.runCatching {
                myPageService.withdrawal()
            }.fold(onSuccess = {
                val isWithdrawalSuccess = withdrawalSuccessResponse.value
                _withdrawalSuccessResponse.value = isWithdrawalSuccess ?: false
            }, onFailure = { error ->
                _withdrawalErrorResponse.value =
                    if (error.isConnectException) NO_INTERNET_CONDITION_ERROR else error.message
            })
        }
    }

    private val _isNotificationPermissionValid: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNotificationPermissionValid: LiveData<Boolean> = _isNotificationPermissionValid
    fun setIsNotificationPermissionValid(isNotificationPermissionValid: Boolean) {
        _isNotificationPermissionValid.value = isNotificationPermissionValid
    }
}