package kr.co.nottodo.util.livedata

class MutableSingleLiveData<T> : SingleLiveData<T> {

    constructor()

    constructor(value: T) {
        liveData.value = Event(value)
    }

    /**
     * MutableLiveData의 setValue()를 통해 값을 설정합니다.
     */
    fun setValue(value: T) {
        liveData.value = Event(value)
    }

    /**
     * MutableLiveData의 postValue()를 통해 값을 설정합니다.
     */
    fun postValue(value: T) {
        liveData.postValue(Event(value))
    }
}
