package luke.koz.supainventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import luke.koz.supainventory.navigation.InventoryNavHost
import luke.koz.supainventory.ui.theme.SupaInventoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SupaInventoryTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    InventoryNavHost()
                }
            }
        }
    }
}