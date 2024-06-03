package com.boukhari.cats.data.repo

import com.boukhari.cats.data.backup.BankBackupService
import com.boukhari.cats.data.local.cache.BankCacheDataSource
import com.boukhari.cats.data.remote.BanksService
import com.boukhari.cats.domain.BankRepository
import com.boukhari.cats.domain.model.Account
import com.boukhari.cats.domain.model.Bank
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Named

class BankRepositoryImpl(
    @Named("backup")
    private val banksBackupService: BankBackupService,
    @Named("api")
    private val banksService: BanksService,
    private val cacheDataSource: BankCacheDataSource
) : BankRepository {

    override suspend fun fetchBanks() {
        val bankResponses = try {
            banksService.fetchBanks()
        } catch (e: Exception) {
            // in case of limited connectivity or any other error, we can use the backup service
            banksBackupService.fetchBanks()
        }

        cacheDataSource.saveBanks(bankResponses)
    }

    override fun getBanksFlow(): Flow<List<Bank>> {
        return cacheDataSource.getBanks().map { banks ->
            banks.map { it.toDomainModel() }
        }
    }

    override fun getAccount(accountId: String): Flow<Account> {
        return cacheDataSource.getAccountById(accountId).map { it.toDomainModel() }
    }
}
