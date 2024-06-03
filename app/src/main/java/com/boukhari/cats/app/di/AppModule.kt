package com.boukhari.cats.app.di

import android.content.Context
import com.boukhari.cats.BuildConfig
import com.boukhari.cats.data.backup.BankBackupService
import com.boukhari.cats.data.backup.BankBackupServiceImpl
import com.boukhari.cats.data.local.cache.BankCacheDataSource
import com.boukhari.cats.data.remote.BanksService
import com.boukhari.cats.data.repo.BankRepositoryImpl
import com.boukhari.cats.domain.BankRepository
import com.boukhari.cats.domain.usecase.GetAccountUseCase
import com.boukhari.cats.domain.usecase.GetBanksUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): BanksService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BanksService::class.java)
    }

    @Provides
    @Singleton
    fun provideBackupService(@ApplicationContext context: Context): BankBackupService {
        return BankBackupServiceImpl(context)
    }

    @Provides
    @Singleton
    fun provideBankCacheDataSource(): BankCacheDataSource {
        return BankCacheDataSource()
    }

    @Provides
    @Singleton
    fun provideBankRepository(
        backupService: BankBackupService,
        banksService: BanksService,
        cacheDataSource: BankCacheDataSource
    ): BankRepository {
        return BankRepositoryImpl(backupService, banksService, cacheDataSource)
    }

    @Provides
    fun provideGetBanksUseCase(repository: BankRepository): GetBanksUseCase {
        return GetBanksUseCase(repository)
    }

    @Provides
    fun provideGetOperationsUseCase(
        repository: BankRepository
    ): GetAccountUseCase {
        return GetAccountUseCase(repository)
    }
}
