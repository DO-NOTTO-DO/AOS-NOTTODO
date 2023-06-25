package kr.co.nottodo.presentation.recommendation.viewmodel

import RecommendationActionListDTO
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.remote.api.ServicePool
import timber.log.Timber
import RecommendationMainListDTO

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

    // 추천 메인 목록을 가져오는 함수
    fun fetchRecommendationMainList() {
        viewModelScope.launch {
            try {
                val response = recommendationMainListService.getRecommendationMainList()
                if (response.status == 200) {
                    Timber.d("성공적인 응답.")
                    _mainList.value = response.data
                } else {
                    Timber.e("상태코드 ${response.status}로 응답")
                    _mainList.value = emptyList()
                }
            } catch (e: Exception) {
                Timber.e(e)
                _mainList.value = emptyList()
            }
        }
    }

    // 추천 카테고리 목록을 가져오는 함수
    fun fetchRecommendationCategoryList(id: Int) {
        viewModelScope.launch {
            try {
                val response = recommendationActionListService.getActionCategoryList(id)
                if (response.status == 200) {
                    Timber.d("성공적인 응답")
                    val categoryList = response.data.find { it.id == id }?.recommendActions
                    _categoryList.value = categoryList ?: emptyList()
                } else {
                    Timber.e("상태코드 ${response.status}로 응답")
                    _categoryList.value = emptyList()
                }
            } catch (e: Exception) {
                Timber.e(e)
                _categoryList.value = emptyList()
            }
        }
        _categoryId.value = id
    }
}
