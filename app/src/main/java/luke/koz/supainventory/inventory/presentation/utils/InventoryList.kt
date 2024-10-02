package luke.koz.supainventory.inventory.presentation.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import luke.koz.supainventory.R
import luke.koz.supainventory.inventory.model.GetItemEntry
import luke.koz.supainventory.ui.theme.SupaInventoryTheme

@Composable
fun InventoryList(
    itemList: List<GetItemEntry>,
    onItemClick: (GetItemEntry) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = itemList, key = { it.id }) { item ->
            InventoryItem(item = item,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onItemClick(item) })
        }
    }
}

@Preview
@Composable
private fun InventoryListPreview() {
    SupaInventoryTheme {
        InventoryList(
            itemList = listOf<GetItemEntry>(
                GetItemEntry(0,"name", 1.0f, 5)
            ),
            onItemClick = {},
            contentPadding = PaddingValues(16.dp)
        )
    }
}
