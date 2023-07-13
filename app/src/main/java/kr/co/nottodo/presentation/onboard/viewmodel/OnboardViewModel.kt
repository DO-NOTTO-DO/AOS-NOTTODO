package kr.co.nottodo.presentation.onboard.viewmodel

import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.nottodo.R

class OnboardViewModel : ViewModel() {
    val painList: List<String> = listOf(
        "고치고 싶은 나쁜 습관이 있어요",
        "루틴대로 하루를 보내지 못해요",
        "계획만 세우고 목표를 달성하지 못해요",
        "불필요한 행위로 시간을 뺏겨요",
        "불편함은 없지만 낫투두를 시도하고 싶어요"
    )

    val situationList: List<String> = listOf(
        "언제나", "업무할 때", "공부할 때", "출근할 때", "퇴근할 때", "일어나자마자", "잠깐 쉴 때", "잠들기 직전", "기타"
    )

    private val selectedSituations: MutableLiveData<MutableList<String>> = MutableLiveData(
        mutableListOf()
    )

    fun getSelectedSituations() = selectedSituations.value
    val selectedSituationsCount: MutableLiveData<Int> = MutableLiveData(0)
    val addSituation: (String) -> Unit = { situation ->
        selectedSituations.value?.add(situation)
        selectedSituationsCount.value = selectedSituationsCount.value?.plus(1)
    }
    val removeSituation: (String) -> Unit = { situation ->
        selectedSituations.value?.remove(situation)
        selectedSituationsCount.value = selectedSituationsCount.value?.minus(1)
    }

    val missionList: List<Mission> = listOf(
        Mission(R.drawable.ic_youtube, "취침 전", "유튜브 추천 영상 생각없이 보지 않기"),
        Mission(R.drawable.ic_delivery, "항상", "배민 VIP 탈출하기"),
        Mission(R.drawable.ic_coffee, "기상 직후", "공복에 커피 마시지 않기"),
        Mission(R.drawable.ic_kakao, "업무 중", "불필요한 PC 카톡 하지 않기"),
        Mission(R.drawable.ic_night_meal, "취침 전", "자기 2시간 전 야식 먹지 않기")
    )

    val actionList: List<String> = listOf(
        "배고플 때마다 양치하기", "삶은 계란으로 대신하기", "집에 간식 사두지 않기"
    )

    data class Mission(
        @DrawableRes val image: Int,
        val situation: String,
        val mission: String,
    )
}