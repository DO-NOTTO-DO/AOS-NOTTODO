package kr.co.nottodo.presentation.recommendation.util

import timber.log.Timber

class NotTodoDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement) =
        "${element.fileName}:${element.lineNumber}#${element.methodName}"
}
