package luke.koz.supainventory.inventory.presentation.screenstates

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import luke.koz.supainventory.R
import luke.koz.supainventory.ui.theme.SupaInventoryTheme
import luke.koz.supainventory.utils.presentation.InventoryGenericWaitingScreen

@Composable
fun InventoryListLoadingScreen() {
    InventoryGenericWaitingScreen(
        composable = { it ->
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                Text("Now we wait patiently for the supp-er server to provide the response to diligent gnomes", modifier = it)
                Image(
                    painter = painterResource(id = R.drawable.loading_img),
                    contentDescription = "Loading Image",
                    modifier = Modifier.size(224.dp).padding(horizontal = 32.dp)
                )
            }
        }
    )
}

@Preview
@Composable
private fun LoadingScreenPrev() {
    SupaInventoryTheme {
        InventoryListLoadingScreen()
    }
}