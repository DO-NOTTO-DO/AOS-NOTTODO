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

class HomeBottomCalenderViewModel : ViewModel() {

    private val homeService: HomeService = ServicePool.homeService
//    lateinit var seletedDays: List<ResponseHomeDoAnotherDay.HomeDoAnotherDayDto?>

    private val _seletedDays: MutableLiveData<List<String>> =
        MutableLiveData()
    val seletedDays: LiveData<List<String>> get() = _seletedDays

    //홈 데일리 조회
    private val _postDoAnotherDay: MutableLiveData<String> =
        MutableLiveData()
    val postDoAnotherDay: LiveData<String> get() = _postDoAnotherDay

    fun postDoAnotherDay(missionId: Long, dates: List<String>) {
        viewModelScope.launch {
            runCatching {
                homeService.postDoAnotherDay(missionId, RequestHomeDoAnotherDay(dates))
            }.fold(onSuccess = {
                _postDoAnotherDay.value = it.status.toString()
                _seletedDays.value = dates
                Timber.d("1231233", "${it.data}")
            },
                onFailure = {
                    _postDoAnotherDay.value = it.message
                    Timber.d("error지롱123 ${it.message}")
                })
        }
    }
}
