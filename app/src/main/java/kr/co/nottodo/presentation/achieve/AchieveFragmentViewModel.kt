package kr.co.nottodo.presentation.achieve

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.model.Home.HomeDailyResponse
import kr.co.nottodo.data.remote.api.ServicePool
import kr.co.nottodo.data.remote.api.ServicePool.homeService
import kr.co.nottodo.data.remote.api.home.AchieveService
import kr.co.nottodo.data.remote.model.achieve.ResponseAchieveCalenderDto
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date

class AchieveFragmentViewModel : ViewModel() {
    private val achieveService: AchieveService = ServicePool.achieveService

    // get 각 주의 성공 개수 가져오기
    private val _calenderCount: MutableLiveData<List<ResponseAchieveCalenderDto.Data>> =
        MutableLiveData()
    val calenderCount: LiveData<List<ResponseAchieveCalenderDto.Data>> get() = _calenderCount

    // 다이얼로그 상세보기
    private val _getAchieveDialog: MutableLiveData<List<HomeDailyResponse.HomeDaily>> =
        MutableLiveData()
    val getAchieveDialog: LiveData<List<HomeDailyResponse.HomeDaily>> get() = _getAchieveDialog

    // errorMessage
    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getCalenderRate(date: String) {
        viewModelScope.launch {
            runCatching {
                achieveService.getAchieveCalendery(date)
            }.fold(
                onSuccess = { _calenderCount.value = it.data },
                onFailure = {
                    _errorMessage.value = it.message
                    Timber.d("achieve error지롱 ${it.message}")
                },
            )
        }
    }

    fun getAchieveDialogDaily(date: String) {
        viewModelScope.launch {
            runCatching {
                homeService.getHomeDaily(date)
            }.fold(
                onSuccess = {
                    _getAchieveDialog.value = it.data.toMutableList()
                },
                onFailure = {
                    _errorMessage.value = it.message
                    Timber.d("error지롱 ${it.message}")
                },
            )
        }
    }

    fun formatDateToLocal(date: Date): String {
        return SimpleDateFormat(AchieveFragment.YEAR_PATTERN).format(date)
    }
}
