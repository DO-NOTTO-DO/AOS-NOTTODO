package kr.co.nottodo.presentation.home.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.model.Home.RequestHomeDoAnotherDay
import kr.co.nottodo.data.remote.api.ServicePool
import kr.co.nottodo.data.remote.api.home.HomeService
import kr.co.nottodo.presentation.recommendation.util.getErrorMessage
import timber.log.Timber

class HomeBottomCalenderViewModel : ViewModel() {

    private val homeService: HomeService = ServicePool.homeService

    private val _seletedDays: MutableLiveData<List<String>> =
        MutableLiveData()
    val seletedDays: LiveData<List<String>> get() = _seletedDays

    // post다른 날도 할래요
    private val _postDoAnotherDay: MutableLiveData<String> =
        MutableLiveData()
    val postDoAnotherDay: LiveData<String> get() = _postDoAnotherDay

    // get다른날도 할래요
    private val _getDoAnotherDay: MutableLiveData<List<String>> =
        MutableLiveData()
    val getDoAnotherDay: LiveData<List<String>> get() = _getDoAnotherDay

    // post다른 날도 할래요
    private val _errorMessage: MutableLiveData<String> =
        MutableLiveData()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun postDoAnotherDay(missionId: Long, dates: List<String>) {
        viewModelScope.launch {
            runCatching {
                homeService.postDoAnotherDay(missionId, RequestHomeDoAnotherDay(dates))
            }.fold(
                onSuccess = {
                    _postDoAnotherDay.value = it.status.toString()
                    _seletedDays.value = dates
                    Timber.tag("1231233").d("${it.data}")
                },
                onFailure = {
                    _postDoAnotherDay.value = it.getErrorMessage()
                },
            )
        }
    }

    fun getDoAnotherDay(missionId: Int) {
        viewModelScope.launch {
            runCatching {
                homeService.getDoAnotherDay(missionId)
            }.fold(
                onSuccess = {
                    _getDoAnotherDay.value = it.data
                    Timber.tag("123123").d("${it.data}")
                },
                onFailure = {
//                    _getDoAnotherDay.value = it.
                    Timber.tag("1231233").d("error지롱123 ${it.message}")
                },
            )
        }
    }
}
