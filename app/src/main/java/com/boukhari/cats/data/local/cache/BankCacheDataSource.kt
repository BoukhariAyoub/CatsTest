package com.boukhari.cats.data.local.cache

import com.boukhari.cats.data.local.BankDataSource
import com.boukhari.cats.data.remote.model.AccountResponse
import com.boukhari.cats.data.remote.model.BankResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class BankCacheDataSource : BankDataSource {

    // Normally, we would use a database to store the data, but for the sake of simplicity, we will use a list
    private val bankFlow = MutableStateFlow<List<BankResponse>>(emptyList())

    override fun getBanks(): Flow<List<BankResponse>> {
        return bankFlow.asStateFlow()
    }

    override suspend fun saveBanks(banks: List<BankResponse>) {
        bankFlow.value = banks
    }

    override fun getAccountById(accountId: String): Flow<AccountResponse> {
        return bankFlow.asStateFlow().map { banks ->
            banks.flatMap { it.accounts }
                .find { it.id == accountId }
                ?: throw IllegalStateException("Account not found")
        }
    }
}
