package luke.koz.supainventory.itementry.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import luke.koz.supainventory.inventory.model.GetItemEntry

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
)

class ItemEntryViewModel : ViewModel() {
    private val supabase = createSupabaseClient(
        supabaseUrl = "https://qsdihmtmasiosykepcwm.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFzZGlobXRtYXNpb3N5a2VwY3dtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjY2NjMyMDYsImV4cCI6MjA0MjIzOTIwNn0.ygESpL0irLfa8o3PAvsDTSqtNKVj_sp33bm5k0HlOso"
    ) {
        install(Postgrest)
    }
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    suspend fun saveItem() {
        val localItemEntry : GetItemEntry = GetItemEntry(
            id = ItemUiState().itemDetails.id,
            itemName = ItemUiState().itemDetails.name,
            itemPrice = ItemUiState().itemDetails.price.toFloat(),
            itemQuantity = ItemUiState().itemDetails.quantity.toInt()
        )
       supabase
            .from("items_table")
            .insert(
                mapOf(
                    "id" to 20,//ItemUiState().itemDetails.id,
                    "item_name" to localItemEntry.itemName,
                    "item_price" to localItemEntry.itemPrice,
                    "item_quantity" to localItemEntry.itemQuantity
                )
            ) {
                select()
                single()
            }
            .decodeAs<GetItemEntry>()
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
        }
    }
}