package kr.co.nottodo.presentation.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationPermissionRequestViewModel : ViewModel() {
    private val _completeBtnClickHandler: MutableLiveData<Boolean> = MutableLiveData()
    val completeBtnClickHandler: LiveData<Boolean> get() = _completeBtnClickHandler
    fun onCompleteBtnClick() {
        _completeBtnClickHandler.value = !(completeBtnClickHandler.value ?: false)
    }
}