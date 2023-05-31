package kr.co.nottodo.presentation.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.data.remote.api.ServicePool
import kr.co.nottodo.data.remote.model.RequestTokenDto
import kr.co.nottodo.data.remote.model.ResponseTokenDto
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.FCM_TOKEN
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.KAKAO

class LoginViewModel : ViewModel() {
    private val tokenService by lazy { ServicePool.tokenService }

    private val _getTokenResult: MutableLiveData<ResponseTokenDto> = MutableLiveData()
    val getTokenResult: LiveData<ResponseTokenDto>
        get() = _getTokenResult

    private val _getErrorResult: MutableLiveData<String> = MutableLiveData()
    val getErrorResult: LiveData<String>
        get() = _getErrorResult

    private var socialToken: String? = null
    fun setSocialToken(newSocialToken: String) {
        socialToken = newSocialToken
    }

    fun getToken() {
        viewModelScope.launch {
            kotlin.runCatching {
                tokenService.getToken(
                    KAKAO, RequestTokenDto(
                        socialToken ?: throw NullPointerException(
                            "social token is null"
                        ), SharedPreferences.getString(FCM_TOKEN) ?: throw NullPointerException(
                            "fcm token is null"
                        )
                    )
                )
            }.fold(onSuccess = { _getTokenResult.value = it },
                onFailure = { _getErrorResult.value = it.message })
        }
    }
}