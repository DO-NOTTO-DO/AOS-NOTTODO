package kr.co.nottodo.presentation.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.nottodo.util.livedata.MutableSingleLiveData
import kr.co.nottodo.util.livedata.SingleLiveData

class CommonDialogViewModel : ViewModel() {

    private val _event = MutableSingleLiveData<EventType>()
    val event: SingleLiveData<EventType> = _event

    private val _isCheckBoxChecked = MutableLiveData(false)
    val isCheckBoxChecked: LiveData<Boolean> = _isCheckBoxChecked
    fun onCheckBoxClick() {
        _isCheckBoxChecked.value = !(isCheckBoxChecked.value ?: false)
    }

    fun onBtnClick() {
        _event.postValue(EventType.NAVIGATE)
    }

    fun onDismissTvClick() {
        _event.postValue(EventType.DISMISS)
    }

    enum class EventType {
        NAVIGATE, DISMISS
    }
}