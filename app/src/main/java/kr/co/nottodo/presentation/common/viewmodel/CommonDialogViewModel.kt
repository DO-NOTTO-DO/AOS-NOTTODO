package kr.co.nottodo.presentation.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.nottodo.util.Event
import kr.co.nottodo.util.emit

class CommonDialogViewModel : ViewModel() {
    private val _isCheckBoxChecked = MutableLiveData(false)
    val isCheckBoxChecked: LiveData<Boolean> = _isCheckBoxChecked
    fun changeIsCheckBoxChecked() {
        _isCheckBoxChecked.value = !(isCheckBoxChecked.value ?: false)
    }

    private val _clickBtnEvent = MutableLiveData<Event<Unit>>()
    val clickBtnEvent: LiveData<Event<Unit>> = _clickBtnEvent
    fun onBtnClickEvent() {
        _clickBtnEvent.emit()
    }

    private val _dismissTvClickEvent = MutableLiveData<Event<Unit>>()
    val dismissTvClickEvent: LiveData<Event<Unit>> = _dismissTvClickEvent
    fun onDismissTvClickEvent() {
        _dismissTvClickEvent.emit()
    }
}