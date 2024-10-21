package luke.koz.supainventory.itemdetail.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import luke.koz.supainventory.inventory.model.GetItemEntry
import luke.koz.supainventory.utils.domain.SupabaseUtilities

class ItemDetailsRepository(private val supabase: SupabaseClient = SupabaseUtilities.client) {
    suspend fun getItem(itemId: Int): GetItemEntry? {
        val insertResponse = supabase
            .from(SupabaseUtilities.getTableName)
            .select(columns = Columns.list("id","item_name", "item_price","item_quantity")) {
                filter {
                    GetItemEntry::id eq itemId
                }
            }
            .decodeList<GetItemEntry>()

        return insertResponse.firstOrNull()
    }

    suspend fun deleteItem(itemId: Int) {
        supabase.from(SupabaseUtilities.getTableName).delete {
            filter {
                GetItemEntry::id eq itemId
            }
        }
    }

    suspend fun sellItem(itemId: Int) {
        val item = getItem(itemId) ?: return // If item at given id does not exist, exit early
        if (item.itemQuantity > 0) {
            val updatedQuantity = item.itemQuantity - 1
            supabase
                .from(SupabaseUtilities.getTableName)
                .update(mapOf("item_quantity" to updatedQuantity)) {
                    filter {
                        GetItemEntry::id eq itemId
                    }
                }
        }
    }
}