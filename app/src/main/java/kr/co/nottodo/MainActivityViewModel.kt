package kr.co.nottodo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    val getFirstDateOnAdd: MutableLiveData<String> = MutableLiveData()
}
