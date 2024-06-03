package com.boukhari.cats.data.local

import com.boukhari.cats.data.remote.model.AccountResponse
import com.boukhari.cats.data.remote.model.BankResponse
import com.boukhari.cats.data.remote.model.OperationResponse
import kotlinx.coroutines.flow.Flow

// this will be implemented by the cache or database or any other local data source
interface BankDataSource {
    fun getBanks(): Flow<List<BankResponse>>
    suspend fun saveBanks(banks: List<BankResponse>)
    fun getAccountById(accountId: String): Flow<AccountResponse>
}