package luke.koz.supainventory.itemdetail.domain

import androidx.lifecycle.ViewModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import luke.koz.supainventory.inventory.model.GetItemEntry
import luke.koz.supainventory.utils.domain.SupabaseUtilities

class ItemDetailsViewModel : ViewModel() {
    private val supabase = SupabaseUtilities.client

    suspend fun getItem(givenId:Int) : GetItemEntry{
        val itemEntry = supabase
            .from(SupabaseUtilities.getTableName)
            /**
             *  selects determines not only filtering but data fetched from server.
             */
            .select(columns = Columns.list("id","item_name", "item_price","item_quantity")) {
                filter {
                    GetItemEntry::id eq givenId
                }
            }
            //.decodeSingle<GetItemEntry>()
            .decodeList<GetItemEntry>()
        android.util.Log.d("items[it]", "itemEntry = ${itemEntry[0].itemName}")
        return itemEntry[0]
    }



    suspend fun deleteItem() {}
    fun reduceQuantityByOne() {}
}