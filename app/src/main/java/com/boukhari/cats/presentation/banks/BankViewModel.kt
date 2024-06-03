package com.boukhari.cats.presentation.banks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boukhari.cats.domain.model.BankType
import com.boukhari.cats.domain.usecase.GetBanksUseCase
import com.boukhari.cats.presentation.UiState
import com.boukhari.cats.presentation.models.BankUi
import com.boukhari.cats.presentation.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class BankViewModel @Inject constructor(
    private val getBanksUseCase: GetBanksUseCase
) : ViewModel() {

    private val _expandedStates = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val expandedStates: StateFlow<Map<String, Boolean>> = _expandedStates

    val banks: Flow<UiState<Map<BankType, List<BankUi>>>> = getBanksUseCase.getBanksFlow()
        .map { banksMap ->
            UiState.Success(banksMap.mapValues { entry ->
                entry.value.map { bank ->
                    bank.toUi()
                }
            }) as UiState<Map<BankType, List<BankUi>>>
        }
        .onStart { emit(UiState.Loading) }
        .catch { e -> emit(UiState.Error(e.message ?: "Unknown error")) }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading)

    fun fetchBanks() {
        viewModelScope.launch(Dispatchers.IO) {
            getBanksUseCase.fetchBanks()
        }
    }

    fun toggleBankExpanded(bankName: String) {
        _expandedStates.value = _expandedStates.value.toMutableMap().apply {
            this[bankName] = !(this[bankName] ?: false)
        }
    }
}
