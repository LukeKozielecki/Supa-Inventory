package luke.koz.supainventory.inventory.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import luke.koz.supainventory.inventory.model.GetItemEntry

/**
 * Repository class responsible for interacting with the Supabase database.
 * It provides methods to fetch items from the database.
 */
class InventoryRepository(private val supabase: SupabaseClient) {
    /**
     * Suspend function to fetch items from the database asynchronously.
     * @return A list of GetItemEntry fetched from the database, or an empty list in case of an error.
     */
    suspend fun fetchItems(): List<GetItemEntry> {
        return try {
            val results = supabase.from("items_table").select().decodeList<GetItemEntry>()
            results
        } catch (e: Exception) {
            // In case of failure (e.g. no internet, firebase gets the high-ground) return an empty list
            emptyList()
        }
    }
}