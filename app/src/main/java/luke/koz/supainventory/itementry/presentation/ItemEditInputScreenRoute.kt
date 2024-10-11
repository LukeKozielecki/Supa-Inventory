package luke.koz.supainventory.itementry.presentation

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import luke.koz.supainventory.inventory.model.GetItemEntry
import luke.koz.supainventory.itementry.domain.ItemDetails
import luke.koz.supainventory.itementry.domain.ItemEntryViewModel
import luke.koz.supainventory.ui.theme.SupaInventoryTheme
import luke.koz.supainventory.utils.presentation.InventoryTopAppBar

@Serializable data class ItemEditInputScreenRoute (val selectedItemId : Int = -1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ItemEntryViewModel = viewModel(),
    passedItemId : Int? = null
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = "Item edit screen",
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        if (passedItemId != null && passedItemId > 0) {
            LaunchedEffect (passedItemId) {
                val localItem : GetItemEntry = viewModel.getItem(passedItemId)
//                viewModel.itemUiState = ItemUiState(
//                    itemDetails = ItemDetails(
//                        id = localItem.id,
//                        name = localItem.itemName,
//                        price = localItem.itemPrice,
//                        quantity = localItem.itemQuantity
//                    ), true
//                )
                viewModel.updateUiState(itemDetails = ItemDetails(
                    id = localItem.id,
                    name = localItem.itemName,
                    price = localItem.itemPrice.toString(),
                    quantity = localItem.itemQuantity.toString()
                ))
            }
        }
        ItemEntryBody(
            itemUiState = viewModel.itemUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                }
            },
            passedItemId = passedItemId!!,
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
        )
    }
}

@Preview
@Composable
private fun ItemEntryScreenPreview() {
    SupaInventoryTheme {
        ItemEntryScreen(navigateBack = { /*TODO*/ }, onNavigateUp = { /*TODO*/ })
    }
}