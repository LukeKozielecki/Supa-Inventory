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
import androidx.navigation.toRoute
import luke.koz.supainventory.inventory.data.InventoryItemEntry
import luke.koz.supainventory.inventory.domain.InventoryViewModel
import luke.koz.supainventory.inventory.presentation.InventoryScreenOptionSelect
import luke.koz.supainventory.inventory.presentation.InventoryScreenOptionSelectRoute
import luke.koz.supainventory.itemdetail.presentation.ItemDetailsScreenCollector
import luke.koz.supainventory.itemdetail.presentation.ItemDetailsScreenRoute
import luke.koz.supainventory.itementry.presentation.ItemEditInputScreenRoute
import luke.koz.supainventory.itementry.presentation.ItemEntryScreen
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
                viewModel = inventoryViewModel,
                retryAction = { /*TODO*/ },
                navToItemEntry = { navController.navigate(ItemEditInputScreenRoute()) },
                navToItemEdit = {itemId -> navController.navigate(ItemDetailsScreenRoute(itemId = itemId))},
                modifier = modifier,
            )
        }

        composable<ItemEditInputScreenRoute> (
            typeMap = mapOf(
                typeOf<InventoryItemEntry>() to ItemNavType.ItemType,
            )
        ) {
            val args = it.toRoute<ItemEditInputScreenRoute>()
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ItemEntryScreen(
                    navigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateUp = { /*TODO*/ },
                    passedItemId = args.selectedItemId ?: null
                )
            }
        }

        composable<ItemDetailsScreenRoute> {
            val args = it.toRoute<ItemDetailsScreenRoute>()
            ItemDetailsScreenCollector(
                args = args,
                navController = navController,
                modifier = modifier
            )
        }
    }
}