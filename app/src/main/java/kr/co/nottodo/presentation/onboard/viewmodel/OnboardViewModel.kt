package kr.co.nottodo.presentation.onboard.viewmodel

import androidx.lifecycle.ViewModel

class OnboardViewModel : ViewModel() {
    val painList: List<String> =
        listOf(
            "고치고 싶은 나쁜 습관이 있어요",
            "루틴대로 하루를 보내지 못해요",
            "계획만 세우고 목표를 달성하지 못해요",
            "불필요한 행위로 시간을 뺏겨요",
            "불편함은 없지만 낫투두를 시도하고 싶어요"
        )

    val situationList: List<String> =
        listOf(
            "언제나",
            "업무할 때",
            "공부할 때",
            "출근할 때",
            "퇴근할 때",
            "일어나자마자",
            "잠깐 쉴 때",
            "잠들기 직전",
            "기타"
        )
}