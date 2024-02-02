package kr.co.nottodo.domain.repository.withdrawal

import kr.co.nottodo.data.remote.api.ServicePool

class WithdrawalRepository {
    suspend fun withdrawal() = ServicePool.myPageService.withdrawal()
}