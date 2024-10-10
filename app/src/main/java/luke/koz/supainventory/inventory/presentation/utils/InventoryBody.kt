package luke.koz.supainventory.inventory.presentation.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import luke.koz.supainventory.R
import luke.koz.supainventory.inventory.model.GetItemEntry
import luke.koz.supainventory.ui.theme.SupaInventoryTheme
import luke.koz.supainventory.utils.presentation.InventoryGenericWaitingScreen


@Composable
fun InventoryBody(
    itemList: List<GetItemEntry>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(32.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (itemList.isEmpty()) {
            InventoryGenericWaitingScreen(
                composable = { modifier ->
                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Text(
                            "Fetching data from the supp-a server.\nPlease keep patience and do note that diligent gnomes are working tirelessly to earn their living",
                            modifier = modifier
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
        } else {
            InventoryList(
                itemList = itemList,
                onItemClick = onItemClick,
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Preview
@Composable
private fun InventoryBodyPreview() {
    SupaInventoryTheme {
        InventoryBody(
            itemList = listOf(GetItemEntry(0,"name",1.0f,1)),
            onItemClick = { TODO() },
            modifier = Modifier,
            contentPadding = PaddingValues(16.dp)
        )
    }
}