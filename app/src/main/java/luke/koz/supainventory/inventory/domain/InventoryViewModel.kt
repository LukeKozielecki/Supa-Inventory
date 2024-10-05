package luke.koz.supainventory.inventory.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import luke.koz.supainventory.inventory.data.InventoryItemEntry
import luke.koz.supainventory.inventory.model.GetItemEntry
import java.io.IOException

data class InventoryListUiState(val itemList: List<InventoryItemEntry> = listOf())

sealed class ItemListState {
    object Loading : ItemListState()
    data class Success(val animals: List<GetItemEntry>) : ItemListState()
    data class Error(val message: String) : ItemListState()
}

class InventoryViewModel : ViewModel() {

    val inventoryListUiState : StateFlow<InventoryListUiState> = MutableStateFlow(InventoryListUiState())

    private val supabase = createSupabaseClient(
        supabaseUrl = "https://qsdihmtmasiosykepcwm.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFzZGlobXRtYXNpb3N5a2VwY3dtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjY2NjMyMDYsImV4cCI6MjA0MjIzOTIwNn0.ygESpL0irLfa8o3PAvsDTSqtNKVj_sp33bm5k0HlOso"
    ) {
        install(Postgrest)
    }
    var itemsListUiState: ItemListState by mutableStateOf(ItemListState.Loading)
        private set

    private val _items = mutableStateListOf<GetItemEntry>()
    val items: List<GetItemEntry> = _items

    /*private*/ fun setItemListUiState(){
        itemsListUiState = ItemListState.Loading
        fetchItems()
        itemsListUiState = try {
            ItemListState.Success(animals = _items)
        } catch (e: IOException) {
            ItemListState.Error("")
        } /*catch (e: HttpException) {
            AnimalListState.Error("")
        } */catch (e: Exception) {
            ItemListState.Error("")
        }
    }

    private fun fetchItems() {
        viewModelScope.launch(Dispatchers.IO) {
            //todo setup exception handling
            // when no internet connection, this function when called throws exception here,
            // calling try catch didn't give expected result of skipping this code
            /**
             * _items is cleared because it prevents
             * java.lang.IllegalArgumentException: Key "1" was already used...
             */
            _items.clear()
            val results = supabase.from("items_table").select().decodeList<GetItemEntry>()
            _items.addAll(results)
        }
    }

    init {
        setItemListUiState()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                InventoryViewModel()
            }
        }
    }
}