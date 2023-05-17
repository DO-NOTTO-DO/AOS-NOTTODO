package kr.co.nottodo.presentation.home.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.model.Home.HomeDailyResponse
import kr.co.nottodo.data.remote.api.ServicePool
import kr.co.nottodo.data.remote.api.home.HomeService
import timber.log.Timber

class HomeViewModel() : ViewModel() {

    private val postService: HomeService = ServicePool.homeService

    //리사이클러뷰 조회
    private val _responseHomeDaily: MutableLiveData<List<HomeDailyResponse.HomeDaily>> =
        MutableLiveData()
    val responseHomeDaily: LiveData<List<HomeDailyResponse.HomeDaily>> get() = _responseHomeDaily

    fun initHome(date: String) {
        viewModelScope.launch {
            runCatching {
                postService.getHomeDaily(date)
            }.fold(onSuccess = { _responseHomeDaily.value = it.data },
                onFailure = {
                    Timber.d("error지롱")
                    Timber.d("why? ${it.message}")
                })
        }
    }

}