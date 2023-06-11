package kr.co.nottodo.presentation.recommendation.viewmodel

import RecommendationActionListDTO
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.remote.api.ServicePool
import retrofit2.http.Path
import timber.log.Timber


class RecommendationViewModel : ViewModel() {
    private val recommendationMainListService by lazy { ServicePool.recommendationMainListService }
    private val recommendationActionListService by lazy { ServicePool.recommendationActionListService }

    private val _mainList: MutableLiveData<List<RecommendationMainListDTO.MainList>> =
        MutableLiveData()
    val mainList: LiveData<List<RecommendationMainListDTO.MainList>>
        get() = _mainList

    private val _categoryList: MutableLiveData<List<RecommendationActionListDTO.ActionList.CategoryList>> =
        MutableLiveData()
    val categoryList: LiveData<List<RecommendationActionListDTO.ActionList.CategoryList>>
        get() = _categoryList

    private val _categoryId: MutableLiveData<Int> = MutableLiveData()
    val categoryId: LiveData<Int> = _categoryId

    init {
        fetchRecommendationMainList()

    }


    fun fetchRecommendationMainList() {
        viewModelScope.launch {
            runCatching {
                recommendationMainListService.getRecommendationMainList()
            }.onSuccess { response ->
                when (response.status) {
                    200 -> {
                        _mainList.value = response.data
                    }

                    else -> {
                        _mainList.value = emptyList()
                    }
                }
            }.onFailure { throwable ->
                Timber.e(throwable)
                _mainList.value = emptyList()
            }
        }
    }


    fun fetchRecommendationCategoryList(id: Int) {
        viewModelScope.launch {
            runCatching {
                recommendationActionListService.getActionCategoryList(id)
            }.onSuccess { response ->
                when (response.status) {
                    200 -> {
                        val categoryList = response.data.find { it.id == id }?.recommendActions
                        _categoryList.value = categoryList ?: emptyList()
                    }

                    else -> {
                        _categoryList.value = emptyList()
                    }
                }
            }.onFailure { throwable ->
                Timber.e(throwable)
                _categoryList.value = emptyList()
            }
        }
    }

    init {
        fetchRecommendationCategoryList(0) // 예시로 0을 전달하여 호출
    }

}
