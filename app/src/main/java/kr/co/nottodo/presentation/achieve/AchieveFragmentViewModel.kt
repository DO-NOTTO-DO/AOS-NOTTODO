package kr.co.nottodo.presentation.achieve

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.remote.api.ServicePool
import kr.co.nottodo.data.remote.api.home.AchieveService
import kr.co.nottodo.data.remote.model.achieve.ResponseAchieveCalenderDto
import timber.log.Timber

class AchieveFragmentViewModel : ViewModel() {
    private val achieveService: AchieveService = ServicePool.achieveService

    //get 각 주의 성공 개수 가져오기
    private val _calenderCount: MutableLiveData<List<ResponseAchieveCalenderDto.Data>> =
        MutableLiveData()
    val calenderCount: LiveData<List<ResponseAchieveCalenderDto.Data>> get() = _calenderCount


    fun getCalenderRate(date: String) {
        viewModelScope.launch {
            runCatching {
                achieveService.getAchieveCalendery(date)
            }.fold(onSuccess = { _calenderCount.value = it.data },
                onFailure = {
                    Timber.d("achieve error지롱 ${it.message}")
                })
        }
    }
}