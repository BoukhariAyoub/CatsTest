package com.boukhari.cats.presentation.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boukhari.cats.domain.usecase.GetAccountUseCase
import com.boukhari.cats.presentation.UiState
import com.boukhari.cats.presentation.models.AccountUi
import com.boukhari.cats.presentation.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountScreenViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {

    private val _accountDetailsState: MutableStateFlow<UiState<AccountUi>> =
        MutableStateFlow(UiState.Loading)
    val accountDetailsState: StateFlow<UiState<AccountUi>> get() = _accountDetailsState

    fun loadAccountDetails(accountId: String) {
        viewModelScope.launch {
            getAccountUseCase.getAccountById(accountId)
                .onEach { Log.i("Account", "Got something : $it") }
                .map { account -> UiState.Success(account.toUi()) as UiState<AccountUi> }
                .onStart { emit(UiState.Loading) }
                .catch { e -> emit(UiState.Error(e.message ?: "Unknown error")) }
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    _accountDetailsState.value = it
                }
        }
    }
}
