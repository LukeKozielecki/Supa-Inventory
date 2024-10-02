package luke.koz.supainventory.inventory.presentation.screenstates

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import luke.koz.supainventory.R
import luke.koz.supainventory.ui.theme.SupaInventoryTheme

@Composable
fun InventoryListLoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = "stringResource(R.string.contentdescription_animalloadingscreen_loading)"
    )
}

@Preview
@Composable
private fun LoadingScreenPrev() {
    SupaInventoryTheme {
        InventoryListLoadingScreen()
    }
}