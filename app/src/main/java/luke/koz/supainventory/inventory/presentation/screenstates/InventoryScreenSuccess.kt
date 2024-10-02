package luke.koz.supainventory.inventory.presentation.screenstates

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import luke.koz.supainventory.inventory.domain.InventoryViewModel
import luke.koz.supainventory.inventory.presentation.utils.InventoryBody
import luke.koz.supainventory.ui.theme.SupaInventoryTheme
import luke.koz.supainventory.utils.presentation.InventoryTopAppBar

//@Serializable object InventoryScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreenSuccess(
    modifier: Modifier = Modifier,
    navToItemEntry: () -> Unit,
    navToItemEdit: (Int) -> Unit,
    viewModel: InventoryViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    //val inventoryListUiState by viewModel.inventoryListUiState.collectAsState()
    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar =  {
            InventoryTopAppBar(title = "Home", canNavigateBack = false)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Item Entry"
                )
            }
        },
    ) { innerPadding ->
        InventoryBody(
            itemList = viewModel.items,//inventoryListUiState.itemList,
            onItemClick = navToItemEdit,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}


@Preview
@Composable
private fun InventoryScreenPreview() {
    SupaInventoryTheme {
        InventoryScreenSuccess(navToItemEntry = {}, navToItemEdit = {})
    }
}