package luke.koz.supainventory.itemdetail.presentation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = {  },
        title = { Text("Attention") },
        text = { Text("Are you sure you want to delete this item? This action is irreversible.\n\nOnce the gnomes are off, there is no turing back. Some say it's re reason people say 'the ship has been gnomed', no wait...") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text("Go back")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text("I am sure")
            }
        }
    )
}