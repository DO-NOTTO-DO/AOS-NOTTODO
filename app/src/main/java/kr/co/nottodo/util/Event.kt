package kr.co.nottodo.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class Event<out T>(val content: T) {

    /** 해당 Event가 다뤄진 이미 다뤄진 Event인지 판별하는 역할을 합니다. */
    private var hasBeenHandled = false

    /** Event가 활성화된 상태인지를 확인합니다.
     * 아직 이벤트 소비가 되지 않았을 경우 true를, 그 외에는 false를 반환합니다. */
    val isActive: Boolean
        get() = if (hasBeenHandled) {
            false
        } else {
            hasBeenHandled = true
            true
        }
}

/** 기존 observe를 한 번 감싸, 다뤄지지 않았을 경우에만 이벤트를 처리합니다. */
fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, observer: Observer<T>) =
    observe(owner) {
        if (it.isActive) {
            observer.onChanged(it.content)
        }
    }

/** 값을 변화시켜 이벤트를 발행합니다. */
fun MutableLiveData<Event<Unit>>.emit() = postValue(Event(Unit))
fun <T> MutableLiveData<Event<T>>.emit(value: T) = postValue(Event(value))