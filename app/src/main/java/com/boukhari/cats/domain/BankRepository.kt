package com.boukhari.cats.domain

import com.boukhari.cats.domain.model.Account
import com.boukhari.cats.domain.model.Bank
import com.boukhari.cats.domain.model.Operation
import kotlinx.coroutines.flow.Flow

interface BankRepository {
    suspend fun fetchBanks()
    fun getBanksFlow(): Flow<List<Bank>>
     fun getAccount(accountId: String): Flow<Account>
}