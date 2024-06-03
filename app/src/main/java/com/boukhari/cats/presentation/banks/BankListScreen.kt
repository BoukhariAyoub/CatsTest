package com.boukhari.cats.presentation.banks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.boukhari.cats.R
import com.boukhari.cats.domain.model.BankType
import com.boukhari.cats.presentation.Routes
import com.boukhari.cats.presentation.UiState
import com.boukhari.cats.presentation.composable.ErrorView
import com.boukhari.cats.presentation.composable.LoadingView
import com.boukhari.cats.presentation.models.AccountUi
import com.boukhari.cats.presentation.models.BankUi


@Composable
fun BankListScreen(navController: NavController) {
    val viewModel: BankViewModel = hiltViewModel()
    val state by viewModel.banks.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    val expandedStates by viewModel.expandedStates.collectAsState(emptyMap())

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchBanks()
    }

    when (val uiState = state) {
        is UiState.Error -> ErrorView(message = uiState.message)
        UiState.Loading -> LoadingView()
        is UiState.Success -> BankListContent(
            bankMap = uiState.data,
            expandedStates = expandedStates,
            onExpandClicked = { viewModel.toggleBankExpanded(it) },
            onAccountClick = { navController.navigate(Routes.operationListRoute(it)) })
    }

}

@Composable
fun BankListContent(
    bankMap: Map<BankType, List<BankUi>>,
    expandedStates: Map<String, Boolean>,
    onExpandClicked: (String) -> Unit,
    onAccountClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            Text(
                text = stringResource(id = R.string.title_activity_main),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            bankMap[BankType.CA]?.let { caBanks ->
                item {
                    BankSection(
                        title = stringResource(id = R.string.bank_section_title_ca),
                        banks = caBanks,
                        expandedStates = expandedStates,
                        onExpandClicked = onExpandClicked,
                        onAccountClick = onAccountClick
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.padding(8.dp))
            }

            item {
                bankMap[BankType.OTHER]?.let { otherBanks ->
                    BankSection(
                        title = stringResource(id = R.string.bank_section_title_others),
                        banks = otherBanks,
                        expandedStates = expandedStates,
                        onExpandClicked = onExpandClicked,
                        onAccountClick = onAccountClick
                    )
                }
            }
        }
    }
}

@Composable
fun BankSection(
    title: String,
    banks: List<BankUi>,
    expandedStates: Map<String, Boolean>,
    onExpandClicked: (String) -> Unit,
    onAccountClick: (String) -> Unit,
) {
    Column {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(color = Color.Gray),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 8.dp)
    ) {
        banks.forEach { bank ->
            BankItem(
                bank = bank,
                expanded = expandedStates[bank.name] ?: false,
                onExpandClicked = { onExpandClicked(bank.name) },
                onAccountClick = onAccountClick
            )
        }
    }

}

@Composable
fun BankItem(
    bank: BankUi,
    expanded: Boolean,
    onExpandClicked: () -> Unit,
    onAccountClick: (String) -> Unit
) {

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandClicked() }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = bank.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column {
                bank.accounts.forEach { account ->
                    AccountItem(account, onAccountClick)
                }
            }
        }
    }
}

@Composable
fun AccountItem(account: AccountUi, onAccountClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAccountClick(account.id) }
            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = account.label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${account.balance} €",
            style = MaterialTheme.typography.bodySmall
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}