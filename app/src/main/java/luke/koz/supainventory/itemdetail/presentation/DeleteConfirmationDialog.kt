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
        text = { Text("Are you sure?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text("no")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text("yes")
            }
        }
    )
}