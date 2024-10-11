package luke.koz.supainventory.itemdetail.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import luke.koz.supainventory.inventory.model.GetItemEntry
import luke.koz.supainventory.itemdetail.domain.ItemDetailsViewModel
import luke.koz.supainventory.ui.theme.SupaInventoryTheme
import luke.koz.supainventory.utils.presentation.InventoryTopAppBar

@Serializable data class ItemDetailsScreenRoute(
    val itemId: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    //make item into itemId and make it retrive data from the database
    item : GetItemEntry,
    navigateToEditItem: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ItemDetailsViewModel = viewModel(),
    onSellUpdateItem: (GetItemEntry) -> Unit,
) {
//    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = "title",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditItem(item.id) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit item",
                )
            }
        },
        modifier = modifier,
    ) { innerPadding ->
        ItemDetailsBody(
            itemDetailsUiState = item,
            onSellItem = {
                coroutineScope.launch {
                    viewModel.sellItem(item.id)
//                    navigateBack()
                }
            },
            onSellUpdateItem = { onSellUpdateItem(item) },
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteItem(item.id)
                    navigateBack()
                }
            },
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
private fun ItemDetailsScreenPreview() {
    SupaInventoryTheme {
        ItemDetailsScreen(
            item = GetItemEntry(5, "Banana", 1.0f, 6),
            navigateToEditItem = {},
            navigateBack = {},
            viewModel = TODO(),
            onSellUpdateItem = TODO()
        )
    }
}