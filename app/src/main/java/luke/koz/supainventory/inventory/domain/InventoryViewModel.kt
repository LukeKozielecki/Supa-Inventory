package luke.koz.supainventory.inventory.domain

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import luke.koz.supainventory.inventory.data.InventoryItemEntry
import luke.koz.supainventory.inventory.data.InventoryRepository
import luke.koz.supainventory.inventory.model.GetItemEntry

data class InventoryListUiState(val itemList: List<InventoryItemEntry> = listOf())

sealed class ItemListState {
    object Loading : ItemListState()
    data class Success(val animals: List<GetItemEntry>) : ItemListState()
    data class Error(val message: String) : ItemListState()
}

/**
 * ViewModel for managing the UI-related data of the inventory.
 * It interacts with the InventoryRepository to fetch and store items.
 */
class InventoryViewModel(private val inventoryRepository: InventoryRepository) : ViewModel() {
//    val inventoryListUiState: StateFlow<InventoryListUiState> = MutableStateFlow(InventoryListUiState())

    /**
     * StateFlow to hold the UI state of the item list. Initially set to Loading and handled later.
     */
    private val _itemsListUiState = MutableStateFlow<ItemListState>(ItemListState.Loading)
    /**
     * Exposes the UI state of the items list as an immutable StateFlow.
     */
    val itemsListUiState: StateFlow<ItemListState> = _itemsListUiState

    /**
     * A mutable state list to hold the items fetched from the repository.
     */
    private val _items = mutableStateListOf<GetItemEntry>()
    /**
     * Exposes an immutable list of [GetItemEntry] entries.
     */
    val items: List<GetItemEntry> = _items

    /**
     * Function to set the UI state for the item list.
     *  - Defaults _itemsListUiState.value to .Loading
     *  - Fetches items from the repository
     *  - Update the items list
     */
    fun setItemListUiState() {
        _itemsListUiState.value = ItemListState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val results = inventoryRepository.fetchItems()

            _items.clear()
            _items.addAll(results)

            _itemsListUiState.value = if (results.isNotEmpty()) {
                ItemListState.Success(animals = results)
            } else {
                ItemListState.Error("No items found")
            }
        }
    }

    init {
        setItemListUiState() // Trigger data loading upon ViewModel creation.
    }

    /**
     * Companion object to provide a factory for the ViewModel, for the sake of dependency injection
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val supabase = createSupabaseClient(
                    supabaseUrl = "https://qsdihmtmasiosykepcwm.supabase.co",
                    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFzZGlobXRtYXNpb3N5a2VwY3dtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjY2NjMyMDYsImV4cCI6MjA0MjIzOTIwNn0.ygESpL0irLfa8o3PAvsDTSqtNKVj_sp33bm5k0HlOso"
                ) {
                    install(Postgrest)
                }
                val repository = InventoryRepository(supabase)
                InventoryViewModel(repository)
            }
        }
    }
}