package luke.koz.supainventory.itementry.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import luke.koz.supainventory.inventory.model.GetItemEntry
import kotlin.random.Random

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
    private val supabase = createSupabaseClient(//todo move this to interface as it is needed in many places
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

    @Serializable
    data class ItemId(
        @SerialName(value = "id") val id : Int,)

    /**
     * [generateUniqueId] creates a variable to store supabase response of ID column.
     * than it creates a new list using previous variable, because it needs to be deserialized
     * than it creates a new variable for a purpose of new Id
     * than it enters a loop to generate new id until it succeeds. when too many elements enter database
     *  it will execute indefinitely. 128 elements is the max.
     * than it succeeds at generating new <Int> id and returns it
     */
    private suspend fun generateUniqueId() : Int {
        val currentIds = supabase
            .from("items_table")
            .select(columns = Columns.list("id"))
            .decodeList<ItemId>()
        val idsList: List<Int> = currentIds.map { it.id }
        var newId : Int
        while(true) {//todo fix max size look fun description
            newId = Random.nextInt(0, 127)
            if (!idsList.contains(newId)) {return newId}
        }
          /*note "why not just list.size+1" at id. the idea behind generating random id, is to avoid situation
          where user know "oh, my item is indexed at 18, this means there are at leased 18 (to 19) items
          in this database, or that product Spanish Banana is index 40, followed by French wine at 42,
          implying "there is an item at 41"*/
    }

    suspend fun saveItem() {
        val localItemEntry : GetItemEntry = GetItemEntry(
            id = generateUniqueId(),
            itemName = itemUiState.itemDetails.name,
            itemPrice = itemUiState.itemDetails.price.toFloat(),
            itemQuantity = itemUiState.itemDetails.quantity.toInt()
        )
       supabase
            .from("items_table")
            .insert(localItemEntry)
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
        }
    }
}