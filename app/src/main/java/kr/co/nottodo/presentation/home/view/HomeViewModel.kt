package kr.co.nottodo.presentation.home.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.model.Home.HomeDailyResponse
import kr.co.nottodo.data.model.Home.RequestHomeMissionCheck
import kr.co.nottodo.data.model.Home.ResponseHomeMissionCheckDto
import kr.co.nottodo.data.remote.api.ServicePool
import kr.co.nottodo.data.remote.api.home.HomeService
import timber.log.Timber

class HomeViewModel() : ViewModel() {

    private val homeService: HomeService = ServicePool.homeService

    //홈 데일리 조회
    private val _getHomeDaily: MutableLiveData<List<HomeDailyResponse.HomeDaily>> =
        MutableLiveData()
    val getHomeDaily: LiveData<List<HomeDailyResponse.HomeDaily>> get() = _getHomeDaily

    //투두 patch
    private val _responseCheckResult: MutableLiveData<ResponseHomeMissionCheckDto.HomeMissionCheckDto> =
        MutableLiveData()
    val responseCheckResult: LiveData<ResponseHomeMissionCheckDto.HomeMissionCheckDto> get() = _responseCheckResult

    fun initHome(date: String) {
        viewModelScope.launch {
            runCatching {
                homeService.getHomeDaily(date)
            }.fold(onSuccess = { _getHomeDaily.value = it.data },
                onFailure = {
                    Timber.d("error지롱 ${it.message}")
                })
        }
    }

    fun patchTodo(missionId: Long, isCheck: String) {
        viewModelScope.launch {
            runCatching {
                homeService.patchTodo(missionId, RequestHomeMissionCheck(isCheck))
            }.fold(onSuccess = { _responseCheckResult.value = it.data
                Timber.d("todo 성공이이롱 ${it.message}")},
                onFailure = {
                    Timber.d("todo error지롱 ${it.message}")
                })
        }
    }


}