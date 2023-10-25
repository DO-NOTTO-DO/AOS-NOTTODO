package kr.co.nottodo.presentation.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.remote.api.ServicePool.tokenService
import kr.co.nottodo.data.remote.model.login.RequestTokenDto
import kr.co.nottodo.data.remote.model.login.ResponseTokenDto
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.KAKAO

class LoginViewModel : ViewModel() {

    private val _getTokenResult: MutableLiveData<ResponseTokenDto> = MutableLiveData()
    val getTokenResult: LiveData<ResponseTokenDto>
        get() = _getTokenResult

    private val _getErrorResult: MutableLiveData<String> = MutableLiveData()
    val getErrorResult: LiveData<String>
        get() = _getErrorResult

    fun login(socialToken: String, fcmToken: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                tokenService.getToken(
                    KAKAO, RequestTokenDto(
                        socialToken = socialToken, fcmToken = fcmToken
                    )
                )
            }.fold(onSuccess = { _getTokenResult.value = it },
                onFailure = { _getErrorResult.value = it.message })
        }
    }
}