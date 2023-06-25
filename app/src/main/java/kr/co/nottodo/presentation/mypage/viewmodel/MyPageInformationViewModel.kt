package kr.co.nottodo.presentation.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.remote.api.ServicePool.myPageService
import kr.co.nottodo.data.remote.model.mypage.WithdrawalDto

class MyPageInformationViewModel : ViewModel() {

    private val _withdrawalSuccessResponse: MutableLiveData<WithdrawalDto> = MutableLiveData()
    val withdrawalSuccessResponse: LiveData<WithdrawalDto> = _withdrawalSuccessResponse

    private val _withdrawalErrorResponse: MutableLiveData<String> = MutableLiveData()
    val withdrawalErrorResponse: LiveData<String> = _withdrawalErrorResponse

    fun withdrawal() {
        viewModelScope.launch {
            kotlin.runCatching {
                myPageService.withdrawal()
            }.fold(onSuccess = { response -> _withdrawalSuccessResponse.value = response },
                onFailure = { error -> _withdrawalErrorResponse.value = error.message })
        }
    }

    private val _isNotificationPermissionValid: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNotificationPermissionValid: LiveData<Boolean> = _isNotificationPermissionValid
    fun setIsNotificationPermissionValid(isNotificationPermissionValid: Boolean) {
        _isNotificationPermissionValid.value = isNotificationPermissionValid
    }
}