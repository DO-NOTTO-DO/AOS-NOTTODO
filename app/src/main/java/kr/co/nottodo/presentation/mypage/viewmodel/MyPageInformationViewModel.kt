package kr.co.nottodo.presentation.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPageInformationViewModel : ViewModel() {
    private val _isNotificationPermissionValid: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNotificationPermissionValid: LiveData<Boolean> = _isNotificationPermissionValid
    fun setIsNotificationPermissionValid(isNotificationPermissionValid: Boolean) {
        _isNotificationPermissionValid.value = isNotificationPermissionValid
    }
}