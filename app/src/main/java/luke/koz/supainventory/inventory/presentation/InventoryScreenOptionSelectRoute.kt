package luke.koz.supainventory.inventory.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.Serializable
import luke.koz.supainventory.inventory.domain.InventoryViewModel
import luke.koz.supainventory.inventory.domain.ItemListState
import luke.koz.supainventory.inventory.presentation.screenstates.InventoryListErrorScreen
import luke.koz.supainventory.inventory.presentation.screenstates.InventoryListLoadingScreen
import luke.koz.supainventory.inventory.presentation.screenstates.InventoryScreenSuccess
import luke.koz.supainventory.ui.theme.SupaInventoryTheme

@Serializable object InventoryScreenOptionSelectRoute

@Composable
fun InventoryScreenOptionSelect(
    modifier: Modifier = Modifier,
    viewModel: InventoryViewModel,
    retryAction: () -> Unit,
    navToItemEntry: () -> Unit,
    navToItemEdit: (Int) -> Unit
) {
    val uiState by viewModel.itemsListUiState.collectAsState()
    when(uiState) {
        is ItemListState.Loading -> {
            InventoryListLoadingScreen()
        }
        is ItemListState.Success -> {
            InventoryScreenSuccess(
                navToItemEntry = { navToItemEntry() },
                navToItemEdit = { itemId -> navToItemEdit(itemId) },
                modifier = modifier,
                viewModel = viewModel
            )
        }
        is ItemListState.Error -> {
            InventoryListErrorScreen(
                retryAction = retryAction,
                modifier = modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun InventoryScreenOptionSelectPreview() {
    val inventoryViewModel: InventoryViewModel = viewModel(factory = InventoryViewModel.Factory)
    SupaInventoryTheme {
        InventoryScreenOptionSelect(
            viewModel = inventoryViewModel,
            retryAction = {},
            navToItemEntry = {},
            navToItemEdit = {}
        )
    }
}