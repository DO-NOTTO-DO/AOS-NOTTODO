package kr.co.nottodo.util.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

abstract class SingleLiveData<T> {

    protected val liveData = MutableLiveData<Event<T>>()

    val value get() = liveData.value?.content


    /**
     * 아직 핸들되지 않았을 경우에만 이벤트를 수행합니다.
     * */
    fun observe(owner: LifecycleOwner, onResult: (T) -> Unit) {
        liveData.observe(owner) { it.getContentIfNotHandled()?.let(onResult) }
    }

    /**
     * 이미 핸들된 경우에도 이벤트를 수행합니다.
     * */
    fun observePeek(owner: LifecycleOwner, onResult: (T) -> Unit) {
        liveData.observe(owner) { onResult(it.content) }
    }
}
