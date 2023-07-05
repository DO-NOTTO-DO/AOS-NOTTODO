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
import kr.co.nottodo.data.remote.model.home.ResponHomeMissionDetail
import kr.co.nottodo.data.remote.model.home.ResponseHomeWeekly
import timber.log.Timber

class HomeViewModel : ViewModel() {

    private val homeService: HomeService = ServicePool.homeService

    // 홈 데일리 조회
    private val _getHomeDaily: MutableLiveData<List<HomeDailyResponse.HomeDaily>> =
        MutableLiveData()
    val getHomeDaily: LiveData<List<HomeDailyResponse.HomeDaily>> get() = _getHomeDaily

    // 투두 patch
    private val _patchCheckResult: MutableLiveData<ResponseHomeMissionCheckDto.HomeMissionCheckDto> =
        MutableLiveData()
    val patchCheckResult: LiveData<ResponseHomeMissionCheckDto.HomeMissionCheckDto> get() = _patchCheckResult

    // 위클리 투두 개수 확인
    private val _getHomeWeeklyResult: MutableLiveData<List<ResponseHomeWeekly.HomeMissionPercent>> =
        MutableLiveData()
    val getHomeWeeklyResult: LiveData<List<ResponseHomeWeekly.HomeMissionPercent>> get() = _getHomeWeeklyResult

    // 투두 바텀시트 상세보기
    private val _getHomeBottomDetail: MutableLiveData<ResponHomeMissionDetail.HomeMissionDetail> =
        MutableLiveData()
    val getHomeBottomDetail: LiveData<ResponHomeMissionDetail.HomeMissionDetail> get() = _getHomeBottomDetail

    // 투두 바텀시트 삭제하기
    private val _deleteTodo: MutableLiveData<Boolean> =
        MutableLiveData()
    val clickDay: MutableLiveData<String> = MutableLiveData()

    fun getHomeDaily(date: String) {
        viewModelScope.launch {
            runCatching {
                homeService.getHomeDaily(date)
            }.fold(
                onSuccess = {
                    _getHomeDaily.value = it.data.toMutableList()
                    Timber.tag("gethomeDaily 성공이이롱 ").d("HomeViewModel - getHomeDaily() : ${it.data}")
                },
                onFailure = {
                    Timber.tag("gethomeDaily error지롱 ").d("${it.message}")
                },
            )
        }
    }

    fun patchTodo(missionId: Long, isCheck: String) {
        viewModelScope.launch {
            runCatching {
                homeService.patchTodo(missionId, RequestHomeMissionCheck(isCheck))
            }.fold(
                onSuccess = {
                    _getHomeDaily.value = getHomeDaily.value?.map { homeDaily ->
                        if (homeDaily.id == missionId) {
                            return@map homeDaily.copy(
                                completionStatus = HomeDailyResponse.HomeDaily.Checked.reverseCheck(
                                    homeDaily.isChecked,
                                ),
                            )
                        } else {
                            return@map homeDaily
                        }
                    }
                    _patchCheckResult.value = it.data
                    Timber.d("todo 성공이이롱 ${it.data}")
                },
                onFailure = {
                    Timber.d("todo error지롱 ${it.message}")
                },
            )
        }
    }

    fun getHomeWeekly(startDate: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                homeService.getHomeWeekly(startDate)
            }.fold(
                onSuccess = {
                    _getHomeWeeklyResult.value = it.data
                    Timber.d("weekly 성공이이롱 ${it.data}")
                },
                onFailure = {
                    Timber.d("weekly error지롱 ${it.message}")
                },
            )
        }
    }

    fun getHomeBottomDetail(missionId: Long) {
        viewModelScope.launch {
            kotlin.runCatching {
                homeService.getHomeBottomDetail(missionId)
            }.fold(
                onSuccess = {
                    _getHomeBottomDetail.value = it.data
                    Timber.d("bottom 성공이이롱 ${it.data}")
                },
                onFailure = {
                    Timber.d("bottom error지롱 ${it.message}")
                },
            )
        }
    }

    suspend fun deleteTodo(missionId: Long) =
            /*
            lifecycleScope.launch {
                val deleteJob = async { postClickDelete(requireArguments().getLong(MISSION_ID)) }
                deleteJob.await()
                dialogDismissListener?.onDeleteButtonClicked()
                dismiss()
            }
             */

//        viewModelScope.launch {
//            val sync =
//                async {
//                    kotlin.runCatching { homeService.deleteTodo(missionId) }.onSuccess {
//                        _deleteTodo.value = true
//                    }
//                }
//            sync.await()
//        }
        viewModelScope.launch {
            runCatching {
                homeService.deleteTodo(missionId)
            }.fold(
                onSuccess = {
                    _deleteTodo.value = true
                    Timber.tag("delete").e("${it.message}")
                },
                onFailure = {
                    Timber.d("delete error지롱 ${it.message}")
                },
            )
        }
//        _deleteTodo.value = false
}
