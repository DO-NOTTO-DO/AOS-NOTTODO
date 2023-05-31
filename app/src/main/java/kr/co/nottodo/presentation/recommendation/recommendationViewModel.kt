package kr.co.nottodo.presentation.toplevel.recommendation.viewmodel

import RecommendationActionListDTO
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.nottodo.data.remote.api.ServicePool
import timber.log.Timber

class RecommendationViewModel : ViewModel() {
    private val recommendationActionTitleService by lazy { ServicePool.recommendationActionTitleService }
    private val recommendationActionListService by lazy { ServicePool.recommendationActionListService }

    private val _actionTitleList: MutableLiveData<List<RecommendationActionListDTO.ActionList>> =
        MutableLiveData()
    val actionTitleList: LiveData<List<RecommendationActionListDTO.ActionList>>
        get() = _actionTitleList

    private val _actionList: MutableLiveData<List<RecommendationActionListDTO.ActionList.CategoryList>> =
        MutableLiveData()
    val actionList: LiveData<List<RecommendationActionListDTO.ActionList.CategoryList>>
        get() = _actionList


    private val _categoryId: MutableLiveData<Int> = MutableLiveData()
    val categoryId: LiveData<Int> = _categoryId

    init {
        viewModelScope.launch {
            runCatching {
                recommendationActionTitleService.getActionTitleList()
            }.onSuccess { response ->
                when (response.status) {
                    200 -> {
                        _actionTitleList.value = response.data
                    }

                    else -> {
                        _actionTitleList.value = emptyList()
                    }
                }
            }.onFailure { throwable ->
                Timber.e(throwable)
                _actionTitleList.value = emptyList()
            }
        }
    }

    fun setCategoryId(id: Int) {
        _categoryId.value = id
    }

    fun getRecommendationList(id: Int) {
        viewModelScope.launch {
            runCatching {
                recommendationActionListService.getActionList(id)
            }.onSuccess { response ->
                when (response.status) {
                    200 -> {
                        _actionList.value = response.data[id].recommendActions
                    }
                    else -> {
                        _actionList.value = emptyList()
                    }
                }
            }.onFailure { throwable ->
                Timber.e(throwable)
                _actionList.value = emptyList()
            }
        }
    }
}