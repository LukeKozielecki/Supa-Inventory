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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import luke.koz.supainventory.inventory.model.GetItemEntry
import luke.koz.supainventory.inventory.presentation.InventoryScreenOptionSelect
import luke.koz.supainventory.inventory.presentation.InventoryScreenOptionSelectRoute
import luke.koz.supainventory.itemdetail.domain.ItemDetailsViewModel
import luke.koz.supainventory.itemdetail.presentation.ItemDetailsScreen
import luke.koz.supainventory.itemdetail.presentation.ItemDetailsScreenRoute
import luke.koz.supainventory.itementry.presentation.ItemEntryScreen
import luke.koz.supainventory.itementry.presentation.ItemEntryScreenRoute
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
                uiState = inventoryViewModel.itemsListUiState,
                viewModel = inventoryViewModel,
                retryAction = { /*TODO*/ },
                navToItemEntry = { navController.navigate(ItemEntryScreenRoute) },
                navToItemEdit = {itemId -> navController.navigate(ItemDetailsScreenRoute(itemId = itemId))},
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

        composable<ItemDetailsScreenRoute>{
            val args = it.toRoute<ItemDetailsScreenRoute>()
            val localViewModel : ItemDetailsViewModel = viewModel()
            val item = remember { mutableStateOf<GetItemEntry?>(null) }
            //todo add mutable uistate
            var isLoadingBoolean by remember { mutableStateOf(true) }
            LaunchedEffect (args.itemId){
                android.util.Log.d("remember","isLoadingBoolean is $isLoadingBoolean")
                android.util.Log.d("items[it]", "On composition: viewModel.items[it].id = \n${localViewModel.getItem(args.itemId).id}, name = ${localViewModel.getItem(args.itemId).id}, price = ${localViewModel.getItem(args.itemId).itemPrice}, quantity ${localViewModel.getItem(args.itemId).itemQuantity},")
                item.value = localViewModel.getItem(args.itemId)
                isLoadingBoolean = item.value == null
                android.util.Log.d("remember","isLoadingBoolean is $isLoadingBoolean")
                android.util.Log.d("items[it]", "viewModel.items[it].id = \n${localViewModel.getItem(args.itemId).id}, name = ${localViewModel.getItem(args.itemId).id}, price = ${localViewModel.getItem(args.itemId).itemPrice}, quantity ${localViewModel.getItem(args.itemId).itemQuantity},")
            }
            when (isLoadingBoolean) {
                true -> {
                    InventoryGenericWaitingScreen(
                        composable = { modifier ->
                            Column (horizontalAlignment = Alignment.CenterHorizontally){
                                Text("Now we wait patiently for the supp-er server to provide the response to diligent gnomes", modifier = modifier)
                                Image(
                                    painter = painterResource(id = R.drawable.loading_img),
                                    contentDescription = "Loading Image",
                                    modifier = Modifier.size(224.dp).padding(horizontal = 32.dp)
                                )
                            }
                        }
                    )
                } else -> {
                    item.value?.let { itemEntry ->
                    ItemDetailsScreen(
                            item = itemEntry,
                            navigateToEditItem = {},
                            navigateBack = { navController.popBackStack() },
                            modifier = modifier,
                            viewModel = localViewModel
                        )
                    } ?: run {
                        Text("Something went very wrong during the loading item process")
                    }
                }
            }
        }
    }
}