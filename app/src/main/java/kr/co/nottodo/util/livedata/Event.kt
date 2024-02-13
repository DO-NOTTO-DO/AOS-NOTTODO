package kr.co.nottodo.util.livedata

class Event<out T>(val content: T) {

    /** 해당 Event가 다뤄진 이미 다뤄진 Event인지 판별하는 역할을 합니다. */
    var hasBeenHandled = false
        private set

    /** Event가 핸들된 상태인지를 확인합니다.
     * 아직 이벤트 소비가 되지 않았을 경우 값을, 그 외에는 null을 반환합니다. */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * 이미 핸들 되었더라도 값을 반환합니다.
     */
    fun peekContent(): T = content
}