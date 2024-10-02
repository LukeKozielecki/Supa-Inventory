package luke.koz.supainventory.inventory.presentation.screenstates

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import luke.koz.supainventory.R
import luke.koz.supainventory.ui.theme.SupaInventoryTheme

@Composable
fun InventoryListErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = "stringResource(R.string.content_description_animal_error_screen)", modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text("stringResource(R.string.content_description_animal_error_screen)")
        }
    }
}
@Preview
@Composable
private fun ErrorScreenPrev() {
    SupaInventoryTheme {
        InventoryListErrorScreen(
            retryAction = {}
        )
    }
}