package luke.koz.supainventory.itemdetail.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import luke.koz.supainventory.inventory.model.GetItemEntry
import luke.koz.supainventory.itemdetail.data.ItemDetailsRepository

class ItemDetailsViewModel (private val itemDetailsRepository: ItemDetailsRepository)  : ViewModel() {
//    private val supabase = SupabaseUtilities.client
    private val _itemDetails = MutableStateFlow<GetItemEntry?>(null)
    val itemDetails: StateFlow<GetItemEntry?> get() = _itemDetails

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun getItem(itemId: Int) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val item = itemDetailsRepository.getItem(itemId)
                _itemDetails.value = item
                if (item == null) {
                    _error.value = "Item not found"
                }
            } catch (e: Exception) {
                _error.value = "Error fetching item: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteItem(itemId: Int) {
        viewModelScope.launch {
            try {
                itemDetailsRepository.deleteItem(itemId)
            } catch (e: Exception) {
                _error.value = "Error deleting item: ${e.message}"
            }
        }
    }

    fun sellItem(itemId: Int) {
        viewModelScope.launch {
            try {
                val item = itemDetailsRepository.getItem(itemId)
                item?.let {
                    if (it.itemQuantity > 0) {
                        itemDetailsRepository.sellItem(it.id)
                        _itemDetails.value = it.copy(itemQuantity = it.itemQuantity - 1)
                    } else {
                        _error.value = "Item is out of stock"
                    }
                } ?: run {
                    _error.value = "Item not found"
                }
            } catch (e: Exception) {
                _error.value = "Error selling item: ${e.message}"
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val supabase = createSupabaseClient(
                    supabaseUrl = "https://qsdihmtmasiosykepcwm.supabase.co",
                    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFzZGlobXRtYXNpb3N5a2VwY3dtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjY2NjMyMDYsImV4cCI6MjA0MjIzOTIwNn0.ygESpL0irLfa8o3PAvsDTSqtNKVj_sp33bm5k0HlOso"
                ) {
                    install(Postgrest)
                }
                val itemDetailsRepository = ItemDetailsRepository(supabase)

                ItemDetailsViewModel(itemDetailsRepository)
            }
        }
    }
}