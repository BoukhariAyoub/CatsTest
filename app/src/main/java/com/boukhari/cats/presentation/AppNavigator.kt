package com.boukhari.cats.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.boukhari.cats.presentation.account.AccountDetailsScreen
import com.boukhari.cats.presentation.banks.BankListScreen

@ExperimentalMaterial3Api
@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.BANK_LIST_SCREEN,
    ) {
        composable(Routes.BANK_LIST_SCREEN) {
            BankListScreen(navController = navController)
        }
        composable(
            route = Routes.OPERATION_LIST_SCREEN,
            arguments = listOf(navArgument("accountId") { type = NavType.StringType })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString("accountId") ?: return@composable
            AccountDetailsScreen(navController = navController, accountId = accountId)
        }
    }
}
