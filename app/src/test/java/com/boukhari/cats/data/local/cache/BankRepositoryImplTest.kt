package com.boukhari.cats.data.local.cache

import com.boukhari.cats.data.backup.BankBackupService
import com.boukhari.cats.data.remote.BanksService
import com.boukhari.cats.data.remote.model.AccountResponse
import com.boukhari.cats.data.remote.model.BankResponse
import com.boukhari.cats.data.repo.BankRepositoryImpl
import com.boukhari.cats.data.repo.toDomainModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.io.IOException

@ExperimentalCoroutinesApi
class BankRepositoryImplTest {

    @Mock
    private lateinit var backupService: BankBackupService

    @Mock
    private lateinit var cacheDataSource: BankCacheDataSource

    @Mock
    private lateinit var apiService: BanksService

    @InjectMocks
    private lateinit var bankRepository: BankRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `fetchBanks should use API service and save to cache`() = runTest {
        // Given
        val bankResponses = listOf(BankResponse("Bank1", 1, listOf()), BankResponse("Bank2", 0, listOf()))
        `when`(apiService.fetchBanks()).thenReturn(bankResponses)

        // When
        bankRepository.fetchBanks()

        // Then
        verify(apiService).fetchBanks()
        verify(cacheDataSource).saveBanks(bankResponses)
    }

    @Test
    fun `fetchBanks should fallback to backup service when API fails`() = runTest {
        // Given
        `when`(apiService.fetchBanks()).thenAnswer { throw IOException("Network error") }
        val backupResponses = listOf(BankResponse("BackupBank1", 1, listOf()), BankResponse("BackupBank2", 0, listOf()))
        `when`(backupService.fetchBanks()).thenReturn(backupResponses)

        // When
        bankRepository.fetchBanks()

        // Then
        verify(apiService).fetchBanks()
        verify(backupService).fetchBanks()
        verify(cacheDataSource).saveBanks(backupResponses)
    }

    @Test
    fun `getBanksFlow should return flow of banks from cache`() = runTest {
        // Given
        val cachedBanks = listOf(BankResponse("Bank1", 1, listOf()), BankResponse("Bank2", 0, listOf()))
        `when`(cacheDataSource.getBanks()).thenReturn(flowOf(cachedBanks))

        // When
        val result = bankRepository.getBanksFlow().first()

        // Then
        verify(cacheDataSource).getBanks()
        assertEquals(cachedBanks.map { it.toDomainModel() }, result)
    }

    @Test
    fun `getAccount should return flow of account from cache`() = runTest {
        // Given
        val accountId = "accountId"
        val cachedAccount = AccountResponse(accountId, "Holder", "Label", "1000", 1, null, null, null, listOf())
        `when`(cacheDataSource.getAccountById(accountId)).thenReturn(flowOf(cachedAccount))

        // When
        val result = bankRepository.getAccount(accountId).first()

        // Then
        verify(cacheDataSource).getAccountById(accountId)
        assertEquals(cachedAccount.toDomainModel(), result)
    }
}
