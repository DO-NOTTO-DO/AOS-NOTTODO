package kr.co.nottodo.presentation.addition.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdditionViewModel : ViewModel() {
    val mission: MutableLiveData<String> = MutableLiveData()
    val situation: MutableLiveData<String> = MutableLiveData()
    val action: MutableLiveData<String> = MutableLiveData()
    val goal: MutableLiveData<String> = MutableLiveData()
}