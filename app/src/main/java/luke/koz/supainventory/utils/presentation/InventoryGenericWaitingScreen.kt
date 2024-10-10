package luke.koz.supainventory.utils.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryGenericWaitingScreen(modifier: Modifier = Modifier, composable : @Composable (Modifier) -> Unit) {
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = "Loading",
                canNavigateBack = true,
                navigateUp = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*todo*/ },
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
        Box (modifier=modifier.padding(paddingValues = innerPadding)){
            composable(modifier.padding(horizontal = 32.dp))
        }
    }
}