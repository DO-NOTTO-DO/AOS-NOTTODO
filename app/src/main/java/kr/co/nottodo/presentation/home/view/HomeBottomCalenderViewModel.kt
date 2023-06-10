package kr.co.nottodo.presentation.home.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.model.Home.RequestHomeDoAnotherDay
import kr.co.nottodo.data.remote.api.ServicePool
import kr.co.nottodo.data.remote.api.home.HomeService
import timber.log.Timber

class HomeBottomCalenderViewModel() : ViewModel() {

    private val homeService: HomeService = ServicePool.homeService

    //홈 데일리 조회
    private val _postDoAnotherDay: MutableLiveData<String> =
        MutableLiveData()
    val postDoAnotherDay: LiveData<String> get() = _postDoAnotherDay


    fun postDoAnotherDay(missionId: Long, dates: List<RequestHomeDoAnotherDay>) {
        viewModelScope.launch {
            runCatching {
                homeService.postDoAnotherDay(missionId, dates)
            }.fold(onSuccess = { _postDoAnotherDay.value = it.message },
                onFailure = {
                    Timber.d("error지롱 ${it.message}")
                })
        }
    }
}