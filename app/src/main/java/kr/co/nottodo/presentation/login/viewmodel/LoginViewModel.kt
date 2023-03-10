package kr.co.nottodo.presentation.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.remote.api.ServicePool
import kr.co.nottodo.data.remote.model.RequestTokenDto
import kr.co.nottodo.data.remote.model.ResponseTokenDto

class LoginViewModel : ViewModel() {
    private val tokenService by lazy { ServicePool.tokenService }

    private val _getTokenResult: MutableLiveData<ResponseTokenDto> = MutableLiveData()
    val getTokenResult: LiveData<ResponseTokenDto>
        get() = _getTokenResult

    private val _getErrorResult: MutableLiveData<String> = MutableLiveData()
    val getErrorResult: LiveData<String>
        get() = _getErrorResult

    fun getToken(request: RequestTokenDto) {
        viewModelScope.launch {
            kotlin.runCatching {
                tokenService.getToken(request)
            }.fold(onSuccess = { _getTokenResult.value = it },
                onFailure = { _getErrorResult.value = it.message })
        }
    }
}