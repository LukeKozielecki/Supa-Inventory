package luke.koz.supainventory.itemdetail.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import luke.koz.supainventory.R
import luke.koz.supainventory.itemdetail.domain.ItemDetailsViewModel
import luke.koz.supainventory.itementry.presentation.ItemEditInputScreenRoute
import luke.koz.supainventory.utils.presentation.InventoryGenericWaitingScreen

@Composable
fun ItemDetailsScreenCollector(
    args: ItemDetailsScreenRoute,
    navController: NavHostController,
    modifier: Modifier
) {
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
                composable = { it ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            stringResource(R.string.please_wait_message),
                            modifier = it
                        )
                        Image(
                            painter = painterResource(id = R.drawable.loading_img),
                            contentDescription = "Loading Image",
                            modifier = Modifier
                                .size(224.dp)
                                .padding(horizontal = 32.dp)
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