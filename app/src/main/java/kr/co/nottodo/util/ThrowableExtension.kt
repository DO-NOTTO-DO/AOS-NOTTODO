package kr.co.nottodo.util

import kotlinx.serialization.json.Json
import kr.co.nottodo.data.remote.model.FailureResponseDto
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException

fun Throwable.getErrorMessage(): String {
    when (this) {
        is HttpException -> {
            val data = Json.decodeFromString<FailureResponseDto>(
                this.response()?.errorBody()?.string() ?: return "예기치 못한 에러 발생"
            )
            return data.message
        }

        else -> {
            Timber.e(this.message.toString())
            return "예기치 못한 에러 발생"
        }
    }
}

val Throwable.isConnectException
    get() = this is ConnectException