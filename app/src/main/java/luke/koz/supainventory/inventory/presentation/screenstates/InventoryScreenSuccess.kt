package luke.koz.supainventory.inventory.presentation.screenstates

import android.util.Log
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
        fun logItems() {
            // Check if the items list is not null or empty
            if (viewModel.items.isNotEmpty()) {
                // Iterate through each item in the items list
                for (item in viewModel.items) {
                    // Log the id of each item
                    Log.d("remember", "Log all items: argsId = ${item.id} at index ${viewModel.items.indexOf(item)}")
                }
            } else {
                Log.d("remember", "No items to log")
            }
        }
        InventoryBody(
            itemList = viewModel.items,//inventoryListUiState.itemList,
            onItemClick = {
                /**
                 * rubber duckie of the lake, what do we want
                 *  we want item id from .items list.
                 *  take that list. at index it. take id parameter
                 */
                android.util.Log.d("remember", "Log this specific item: argsId = ${viewModel.items[it].id}, viewModel.items[it] = ${viewModel.items[it]}")
                logItems()
                Log.d("it", "it = $it")
                /**
                 * the correct way of applying this lambda is to directly take the... [it]
                 * representing position on the list
                 */
                navToItemEdit(it)
                android.util.Log.d("items[it]", "viewModel.items[it].id = ${viewModel.items[it].id}")
                viewModel.setItemListUiState()
                          },
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}


@Preview
@Composable
private fun InventoryScreenPreview() {
    SupaInventoryTheme {
        InventoryScreenSuccess(navToItemEntry = {}, navToItemEdit = {}, )
    }
}