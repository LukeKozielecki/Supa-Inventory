package luke.koz.supainventory.itemdetail.domain

import androidx.lifecycle.ViewModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import luke.koz.supainventory.inventory.model.GetItemEntry
import luke.koz.supainventory.utils.domain.SupabaseUtilities

class ItemDetailsViewModel : ViewModel() {
    private val supabase = SupabaseUtilities.client

    suspend fun getItem(givenId:Int) : GetItemEntry{
        val insertResponse = supabase
            .from(SupabaseUtilities.getTableName)
            /**
             *  selects determines not only filtering but data fetched from server.
             */
            .select(columns = Columns.list("id","item_name", "item_price","item_quantity")) {
                filter {
                    GetItemEntry::id eq givenId
                }
            }
            .decodeList<GetItemEntry>()
        android.util.Log.d("items[it]", "itemEntry = ${insertResponse[0].itemName}")
        return insertResponse[0]
    }

    suspend fun deleteItem(givenId: Int) : Unit {
        val response = supabase
            .from(SupabaseUtilities.getTableName)
            .delete {
                filter {
                    GetItemEntry::id eq givenId
                }
            }
    }

    suspend fun sellItem(givenId: Int) : Unit {
        val item = getItem(givenId)
        if (item.itemQuantity > 0) {
            val updatedQuantity = item.itemQuantity - 1
            val updateResponse = supabase
                .from(SupabaseUtilities.getTableName)
                .update(
                    mapOf("item_quantity" to updatedQuantity)
                ) {
                    filter {
                        GetItemEntry::id eq givenId
                    }
                }
        }
    }
}