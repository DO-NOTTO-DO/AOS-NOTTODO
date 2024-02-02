package kr.co.nottodo.presentation.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch
import kr.co.nottodo.domain.repository.withdrawal.WithdrawalRepository
import kr.co.nottodo.util.PublicString.NO_INTERNET_CONDITION_ERROR
import kr.co.nottodo.util.isConnectException

class WithdrawalDialogViewModel : ViewModel() {

    private val withdrawalRepository = WithdrawalRepository()

    private val _withdrawalSuccessResponse: MutableLiveData<Boolean> = MutableLiveData()
    val withdrawalSuccessResponse: LiveData<Boolean> = _withdrawalSuccessResponse

    private val _withdrawalErrorResponse: MutableLiveData<String> = MutableLiveData()
    val withdrawalErrorResponse: LiveData<String> = _withdrawalErrorResponse

    fun withdrawal() {
        viewModelScope.launch {
            runCatching {
                withdrawalRepository.withdrawal()
            }.fold(onSuccess = { response ->
                _withdrawalSuccessResponse.value = response.isSuccessful
                UserApiClient.instance.unlink {}
            }, onFailure = { error ->
                _withdrawalErrorResponse.value =
                    if (error.isConnectException) NO_INTERNET_CONDITION_ERROR else error.message
            })
        }
    }
}
