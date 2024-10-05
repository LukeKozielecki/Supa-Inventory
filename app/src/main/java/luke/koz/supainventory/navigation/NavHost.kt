package luke.koz.supainventory.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import luke.koz.supainventory.inventory.data.InventoryItemEntry
import luke.koz.supainventory.inventory.domain.InventoryViewModel
import luke.koz.supainventory.inventory.presentation.InventoryScreenOptionSelect
import luke.koz.supainventory.inventory.presentation.InventoryScreenOptionSelectRoute
import luke.koz.supainventory.itementry.presentation.ItemEntryScreen
import luke.koz.supainventory.itementry.presentation.ItemEntryScreenRoute
import kotlin.reflect.typeOf

@Composable
fun InventoryNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = InventoryScreenOptionSelectRoute
    ) {
        composable<InventoryScreenOptionSelectRoute> {
            val inventoryViewModel: InventoryViewModel = viewModel(factory = InventoryViewModel.Factory)
            InventoryScreenOptionSelect(
                uiState = inventoryViewModel.itemsListUiState,
                viewModel = inventoryViewModel,
                retryAction = { /*TODO*/ },
                navToItemEntry = { navController.navigate(ItemEntryScreenRoute) },
                modifier = modifier,
            )
        }

        composable<ItemEntryScreenRoute> (
            typeMap = mapOf(
                typeOf<InventoryItemEntry>() to ItemNavType.ItemType,
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ItemEntryScreen(
                    navigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateUp = { /*TODO*/ }
                )
            }
        }
    }
}