package com.boukhari.cats.data.remote
import com.boukhari.cats.data.remote.model.BankResponse
import retrofit2.http.GET

interface BanksService {
    
    @GET("banks.json")
    suspend fun fetchBanks(): List<BankResponse>
}
