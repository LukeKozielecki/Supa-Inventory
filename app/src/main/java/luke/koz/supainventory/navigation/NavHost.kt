package luke.koz.supainventory.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import luke.koz.supainventory.R
import luke.koz.supainventory.inventory.data.InventoryItemEntry
import luke.koz.supainventory.inventory.domain.InventoryViewModel
import luke.koz.supainventory.inventory.presentation.InventoryScreenOptionSelect
import luke.koz.supainventory.inventory.presentation.InventoryScreenOptionSelectRoute
import luke.koz.supainventory.itemdetail.domain.ItemDetailsViewModel
import luke.koz.supainventory.itemdetail.presentation.ItemDetailsScreen
import luke.koz.supainventory.itemdetail.presentation.ItemDetailsScreenRoute
import luke.koz.supainventory.itementry.presentation.ItemEditInputScreenRoute
import luke.koz.supainventory.itementry.presentation.ItemEntryScreen
import luke.koz.supainventory.utils.presentation.InventoryGenericWaitingScreen
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
            val localViewModel: ItemDetailsViewModel = viewModel(factory = ItemDetailsViewModel.Factory)

            val item by localViewModel.itemDetails.collectAsState()
            val isLoading by localViewModel.loading.collectAsState()
            val errorMessage by localViewModel.error.collectAsState()

            LaunchedEffect(args.itemId) {
                localViewModel.getItem(args.itemId)
            }

//            // Check for loading state; you might consider using the Loading LiveData here
//            isLoading = localViewModel.loading.observeAsState(false).value ?: true


            when {
                isLoading -> {
                    InventoryGenericWaitingScreen(
                        composable = { modifier ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Now we wait patiently for the supp-er server to provide the response to diligent gnomes",
                                    modifier = modifier
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.loading_img),
                                    contentDescription = "Loading Image",
                                    modifier = Modifier.size(224.dp).padding(horizontal = 32.dp)
                                )
                            }
                        }
                    )
                }
                item != null -> {
                    val itemEntry = item!!
                    ItemDetailsScreen(
                        item = itemEntry,
                        navigateToEditItem = { navController.navigate(ItemEditInputScreenRoute(itemEntry.id)) },
                        navigateBack = { navController.popBackStack() },
                        modifier = modifier,
                        viewModel = localViewModel,
                        onSellUpdateItem = { soldItem ->
                            localViewModel.sellItem(soldItem.id)
                        }
                    )
                }
                errorMessage != null -> {
                    Text("Something went very wrong during the loading item process: ${errorMessage}")
                }
                else -> {
                    Text("Unexpected state")
                }
            }
        }
    }
}